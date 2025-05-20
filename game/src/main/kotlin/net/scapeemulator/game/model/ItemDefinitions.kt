package net.scapeemulator.game.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import java.util.*

object ItemDefinitions {
    private val logger = KotlinLogging.logger {}
    private lateinit var definitions: MutableMap<Int, ItemDefinition>

//    @JvmStatic
//    @Throws(IOException::class)
//    fun init(cache: Cache) {
//        var count = 0
//
//        val tableContainer = Container.decode(cache.store.read(255, 19))
//        val table = ReferenceTable.decode(tableContainer.getData())
//
//        val files = table.capacity()
////        definitions = arrayOfNulls(files * 256)
//
//        for (file in 0..<files) {
//            val entry = table.getEntry(file)
//            if (entry == null) continue
//
//            val archive = Archive.decode(cache.read(19, file).getData(), entry.size())
//            var nonSparseMember = 0
//            for (member in 0..<entry.capacity()) {
//                val childEntry = entry.getEntry(member)
//                if (childEntry == null) continue
//
//                val id = file * 256 + member
//                val definition = ItemDefinition.decode(archive.getEntry(nonSparseMember++)!!)
//
//                definitions[id] = definition
//                count++
//            }
//        }
//        logger.info { "Loaded $count item definitions." }
//    }

    fun init(file: File) {
        val mapper: ObjectMapper = jacksonObjectMapper()
        val map: MutableMap<Int, ItemDefinition> =
            mapper.readValue(
                file,
                object : TypeReference<MutableMap<Int, ItemDefinition>>() {}
            )
        definitions = map
        logger.info { "Loaded ${definitions.size} item definitions" }
    }

    fun forId(id: Int): ItemDefinition? {
        if (definitions[id] == null) {
            logger.debug { "Unknown item definition $id" }
        }
        return definitions[id]
    }
}

data class SpecialHandler(val item: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemDefinition(
    val id: Integer,
    val name: String,
    val examine: String,
    val options: Set<String>,
    val configurations: Map<String, Object>,
    val stackable: Boolean,
    val value: Int,
    val membersOnly: Boolean,
    val equipmentId: Int,
    val itemRequirements: Map<Integer, Integer>?,
    val renderAnimationId: Int,
    val groundOptions: Set<String>,
    val inventoryOptions: Set<String>,
    val maxValue: Int,
    val minValue: Int,
    private val tradeable: Boolean,
    private val isFullHelm: Boolean,
    private val isFullMask: Boolean,
    private val isFullBody: Boolean,
    val lendable: Boolean,
    val highAlchemy: Int,
    val lowAlchemy: Int,
    val destroyable: Boolean,
    val shopPrice: Int,
    val grandExchangePrice: Int,
    val weight: Double,
    val bonuses: IntArray?,
    val absorb: IntArray?,
    val isTwoHand: Boolean,
    val equipmentSlot: Int,
    val attackSpeed: Int,
    val standAnimation: Int,
    val standTurnAnimation: Int,
    val walkAnimation: Int?,
    val runAnimation: Int,
    val turn180Animation: Int,
    val turn90CWAnimation: Int,
    val turn90CCWAnimation: Int,
    val weaponInterface: Int,
    val hasSpecial: Boolean,
    val destroyMessage: String?,
    val grandExchangeLimit: Int,
    val defenseAnimation: Int,
    val pointPrice: Int,
    val tradeOverride: Boolean,
    val tokkulPrice: Int,
    val funWeapon: Boolean,
    val renderAnimation: Int,
    val attackAnimations: IntArray?,
    val attackSounds: IntArray?,
    val maleWornModelId1: Int
) {

    fun <V> getConfig(key: String, fail: V): V {
        val value = configurations[key] as V
        return value ?: fail
    }

    fun isTradable(): Boolean {
        return tradeable && getConfig(CONFIG_TRADEABLE, false)
    }

    fun isFullHelm(): Boolean {
        check(equipmentSlot == Equipment.HEAD)
        return isFullHelm
    }

    fun isFullMask(): Boolean {
        check(equipmentSlot == Equipment.HEAD)
        return isFullMask
    }

    fun isFullBody(): Boolean {
        check(equipmentSlot == Equipment.BODY)
        return isFullBody
    }

    fun getWeaponClass(): WeaponClass {
        check(equipmentSlot == Equipment.WEAPON)
        val name = name.lowercase(Locale.getDefault())
        if (name.contains("scythe")) return WeaponClass.SCYTHE
        if (name.contains("pickaxe")) return WeaponClass.PICKAXE
        if (name.contains("axe")) return WeaponClass.AXE
        if (name.contains("godsword")) return WeaponClass.GODSWORD
        if (name.contains("claws")) return WeaponClass.CLAWS
        if (name.contains("longsword")) return WeaponClass.SWORD_DAGGER
        if (name.contains("scimitar")) return WeaponClass.SCIMITAR
        if (name.contains("2h sword")) return WeaponClass.TWO_H_SWORD
        if (name.contains("sword")) return WeaponClass.SWORD_DAGGER
        if (name.contains("dagger")) return WeaponClass.SWORD_DAGGER
        if (name.contains("mace") || name.contains("flail")
            || name.contains("anchor")
        ) return WeaponClass.MACE
        if (name.contains("maul") || name.contains("warhammer")) return WeaponClass.MAUL
        if (name.contains("whip")) return WeaponClass.WHIP
        if (name.contains("longbow")) return WeaponClass.BOW
        if (name.contains("bow")) return WeaponClass.BOW
        if (name.contains("staff")) return WeaponClass.STAFF
        if (name.contains("spear")) return WeaponClass.SPEAR
        if (name.contains("dart")) return WeaponClass.THROWN
        return WeaponClass.UNARMED
    }

    fun getAttackAnimation(style: Int): Int {
        val anim: Int? = attackAnimations?.get(style)
        var anims = emptyArray<Int>()
        if (name.contains("warhammer")) {
            arrayOf(400, 401)[style]
        }
        return anim ?: 424 // unarmed
    }

    fun getStance(): Int {
        if (walkAnimation == 0) return 1426
        return renderAnimation
    }

    companion object {
        val CONFIG_TRADEABLE = "tradeable"
        val CONFIG_LENDABLE = "lendable"
        val CONFIG_DESTROY = "destroy"
        val CONFIG_TWO_HANDED = "two_handed"
        val CONFIG_HIGH_ALCHEMY = "high_alchemy"
        val CONFIG_LOW_ALCHEMY = "low_alchemy"
        val CONFIG_SHOP_PRICE = "shop_price"
        val CONFIG_GE_PRICE = "grand_exchange_price"
        val CONFIG_EXAMINE = "examine"
        val CONFIG_WEIGHT = "weight"
        val CONFIG_BONUS = "bonuses"
        val CONFIG_ABSORB = "absorb"
        val CONFIG_EQUIP_SLOT = "equipment_slot"
        val CONFIG_ATTACK_SPEED = "attack_speed"

        val CONFIG_STAND_ANIM = "stand_anim" //Animation.class
        val CONFIG_STAND_TURN_ANIM = "stand-turn_anim"
        val CONFIG_WALK_ANIM = "walk_anim"
        val CONFIG_RUN_ANIM = "run_anim"
        val CONFIG_TURN180_ANIM = "turn180_anim"
        val CONFIG_TURN90CW_ANIM = "turn90cw_anim"
        val CONFIG_TURN90CCW_ANIM = "turn90ccw_anim"
        val CONFIG_WEAPON_INTERFACE = "weapon_interface"
        val CONFIG_HAS_SPECIAL = "has_special"
        val CONFIG_ATTACK_ANIMS = "attack_anims"
        val CONFIG_DESTROY_MESSAGE = "destroy_message"
        val CONFIG_REQUIREMENTS = "requirements"
        val CONFIG_GE_LIMIT = "ge_buy_limit"
        val CONFIG_DEFENCE_ANIMATION = "defence_anim"
        val CONFIG_ATTACK_AUDIO = "attack_audios"
        val CONFIG_POINT_PRICE = "point_price"
        val CONFIG_SPAWNABLE = "spawnable"
        val CONFIG_BANKABLE = "bankable"
        val CONFIG_RARE_ITEM = "rare_item"
        val CONFIG_TOKKUL_PRICE = "tokkul_price"

        val CONFIG_RENDER_ANIM_ID = "render_anim"

//        @Suppress("unused")
//        fun decode(buffer: ByteBuffer): ItemDefinition {
//            def.groundOptions = arrayOf(null, null, "take", null, null)
//            def.inventoryOptions = arrayOf(null, null, null, null, "drop")
//            while (true) {
//                val opcode = buffer.get().toInt() and 0xFF
//                if (opcode == 0) break
//                if (opcode == 1) def.inventoryModelId = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode == 2) def.name = getJagexString(buffer)
//                else if (opcode == 4) def.modelZoom = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode == 5) def.modelRotation1 = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode == 6) def.modelRotation2 = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode == 7) {
//                    def.modelOffset1 = buffer.getShort().toInt() and 0xFFFF
//                    if (def.modelOffset1 > 32767) def.modelOffset1 -= 65536
//                    def.modelOffset1 = def.modelOffset1 shl 0
//                } else if (opcode == 8) {
//                    def.modelOffset2 = buffer.getShort().toInt() and 0xFFFF
//                    if (def.modelOffset2 > 32767) def.modelOffset2 -= 65536
//                    def.modelOffset2 = def.modelOffset2 shl 0
//                } else if (opcode == 11) def.isStackable = true
//                else if (opcode == 12) def.value = buffer.getInt()
//                else if (opcode == 16) def.isMembersOnly = true
//                else if (opcode == 18) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 23) def.maleWearModel1 = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode == 24) def.femaleWearModel1 = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode == 25) def.maleWearModel2 = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode == 26) def.femaleWearModel2 = buffer.getShort().toInt() and 0xFFFF
//                else if (opcode >= 30 && opcode < 35) def.groundOptions[opcode - 30] = getJagexString(buffer)
//                else if (opcode >= 35 && opcode < 40) def.inventoryOptions[opcode - 35] = getJagexString(buffer)
//                else if (opcode == 40) {
//                    val length = buffer.get().toInt() and 0xFF
//                    def.originalModelColors = ShortArray(length)
//                    def.modifiedModelColors = ShortArray(length)
//                    for (index in 0..<length) {
//                        def.originalModelColors[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                        def.modifiedModelColors[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                    }
//                } else if (opcode == 41) {
//                    val length = buffer.get().toInt() and 0xFF
//                    def.textureColour1 = ShortArray(length)
//                    def.textureColour2 = ShortArray(length)
//                    for (index in 0..<length) {
//                        def.textureColour1[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                        def.textureColour2[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                    }
//                } else if (opcode == 42) {
//                    val length = buffer.get().toInt() and 0xFF
//                    for (index in 0..<length) {
//                        val i = buffer.get().toInt()
//                    }
//                } else if (opcode == 65) {
//                    def.isUnnoted = true
//                } else if (opcode == 78) {
//                    def.colourEquip1 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 79) {
//                    def.colourEquip2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 90) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 91) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 92) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 93) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 95) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 96) {
//                    val i = buffer.get().toInt() and 0xFF
//                } else if (opcode == 97) {
//                    def.notedId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 98) {
//                    def.notedTemplateId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode >= 100 && opcode < 110) {
//                    if (def.stackableIds == null) {
//                        def.stackableIds = IntArray(10)
//                        def.stackableAmounts = IntArray(10)
//                    }
//                    def.stackableIds!![opcode - 100] = buffer.getShort().toInt() and 0xFFFF
//                    def.stackableAmounts[opcode - 100] = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 110) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 111) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 112) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 113) {
//                    val i = buffer.get().toInt()
//                } else if (opcode == 114) {
//                    val i = buffer.get() * 5
//                } else if (opcode == 115) {
//                    def.teamId = buffer.get().toInt() and 0xFF
//                } else if (opcode == 121) {
//                    def.lendId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 122) {
//                    def.lendTemplateId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 125) {
//                    val i = buffer.get().toInt() shl 0
//                    val i2 = buffer.get().toInt() shl 0
//                    val i3 = buffer.get().toInt() shl 0
//                } else if (opcode == 126) {
//                    val i = buffer.get().toInt() shl 0
//                    val i2 = buffer.get().toInt() shl 0
//                    val i3 = buffer.get().toInt() shl 0
//                } else if (opcode == 127) {
//                    val i = buffer.get().toInt() and 0xFF
//                    val i2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 128) {
//                    val i = buffer.get().toInt() and 0xFF
//                    val i2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 129) {
//                    val i = buffer.get().toInt() and 0xFF
//                    val i2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 130) {
//                    val i = buffer.get().toInt() and 0xFF
//                    val i2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 132) {
//                    val len = buffer.get().toInt() and 0xFF
//                    for (index in 0..<len) {
//                        val anInt = buffer.getShort().toInt() and 0xFFFF
//                    }
//                } else if (opcode == 249) {
//                    val length = buffer.get().toInt() and 0xFF
//                    for (index in 0..<length) {
//                        val stringInstance = buffer.get().toInt() == 1
//                        val key = getTriByte(buffer)
//                        val value: Any? = if (stringInstance) getJagexString(buffer) else buffer.getInt()
//                    }
//                }
//            }
//            return def
//        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ItemDefinition
        if (id != other.id) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode() + value
    }
}


fun dump() {
    val mapper: ObjectMapper = jacksonObjectMapper()
    // Load from a File
    val file = File("./data/itemDefinitions.json")
    val map: MutableMap<Int, ItemDefinition> =
        mapper.readValue(
            file,
            object : TypeReference<MutableMap<Int, ItemDefinition>>() {}
        )
//    definitions = map
//        val print = ObjectMapper().writeValueAsString(map[5698])
//        println(print)
    KotlinLogging.logger { }.info { "Loaded ${map.size} item definitions" }
}

fun main() {
    dump()
}

