package net.scapeemulator.game.model

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.DataInputStream
import java.io.FileInputStream
import java.io.IOException

class EquipmentDefinition {

    var id: Int = 0
        private set
    var equipmentId: Int = 0
    var slot: Int = 0
        private set

    private var stance = 1426//0
    private var fullBody = false
    private var fullMask = false
    private var fullHelm = false
    private var twoHanded = false

    fun isFullBody(): Boolean {
        check(slot == Equipment.BODY)
        return fullBody
    }

    fun isFullMask(): Boolean {
        check(slot == Equipment.HEAD)
        return fullMask
    }

    fun isFullHelm(): Boolean {
        check(slot == Equipment.HEAD)
        return fullHelm
    }

    fun isTwoHanded(): Boolean {
        check(slot == Equipment.WEAPON)
        return twoHanded
    }

    fun getStance(): Int {
        check(slot == Equipment.WEAPON)
        return stance
    }

    companion object {
        const val FLAG_TWO_HANDED: Int = 0x1
        const val FLAG_FULL_HELM: Int = 0x2
        const val FLAG_FULL_MASK: Int = 0x4
        const val FLAG_FULL_BODY: Int = 0x8

        private val logger = KotlinLogging.logger {}
        private val definitions: MutableMap<Int, EquipmentDefinition> = HashMap()

        @Throws(IOException::class)
        fun init() {
            DataInputStream(FileInputStream("data/equipment.dat")).use { reader ->
                var id: Int
                var nextEquipmentId = 0
                while ((reader.readShort().also { id = it.toInt() }.toInt()) != -1) {
                    val flags = reader.read() and 0xFF
                    val slot = reader.read() and 0xFF
                    var stance = 0
                    var weaponClass = 0
                    if (slot == Equipment.WEAPON) {
                        stance = reader.readShort().toInt() and 0xFFFF
                        weaponClass = reader.read() and 0xFF
                    }

                    val equipment = EquipmentDefinition()
                    equipment.id = id

                    equipment.equipmentId = nextEquipmentId++

                    equipment.slot = slot
                    equipment.twoHanded = (flags and FLAG_TWO_HANDED) != 0
                    equipment.fullHelm = (flags and FLAG_FULL_HELM) != 0
                    equipment.fullMask = (flags and FLAG_FULL_MASK) != 0
                    equipment.fullBody = (flags and FLAG_FULL_BODY) != 0
                    if (slot == Equipment.WEAPON) {
                        equipment.stance = stance
//                        weaponClass = WeaponClass.entries[weaponClass]
                    }

                    definitions[id] = equipment
                }
                logger.info { "Loaded " + definitions.size + " equipment definitions." }

            }
        }

        fun forId(id: Int): EquipmentDefinition? {
            return definitions[id]
        }
    }
}
