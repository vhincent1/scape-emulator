package net.scapeemulator.game.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.cache.Archive
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container
import net.scapeemulator.cache.ReferenceTable
import java.io.File
import java.io.IOException
import java.util.*

//todo item durability
object ItemDefinitions {
    private val logger = KotlinLogging.logger {}

    //    private lateinit var definitions: MutableMap<Int, ItemDefinition>
    private lateinit var definitions: Array<ItemDefinition>

    @JvmStatic
    @Throws(IOException::class)
    fun init(cache: Cache) {
        var count = 0
        val tableContainer = Container.decode(cache.store.read(255, 19))
        val table = ReferenceTable.decode(tableContainer.getData())
        val files = table.capacity()
//        definitions = arrayOfNulls(files * 256)

        for (file in 0..<files) {
            val entry = table.getEntry(file) ?: continue

            val archive = Archive.decode(cache.read(19, file).getData(), entry.size())
            var nonSparseMember = 0
            for (member in 0..<entry.capacity()) {
                val childEntry = entry.getEntry(member) ?: continue

                val id = file * 256 + member
                val definition =
                    net.scapeemulator.cache.def.ItemDefinition.decode(id, archive.getEntry(nonSparseMember++)!!)

//                definitions[id] = definition(id)
                count++
            }
        }
        logger.info { "Loaded $count item definitions." }
    }

    fun init(file: File) {
        val mapper: ObjectMapper = jacksonObjectMapper()
//        val map: MutableMap<Int, ItemDefinition> =
//            mapper.readValue(file, object : TypeReference<MutableMap<Int, ItemDefinition>>() {})
        //        definitions = map
        val map: Array<ItemDefinition> = mapper.readValue(file, object : TypeReference<Array<ItemDefinition>>() {})
        definitions = map
        logger.info { "Loaded ${definitions.size} item definitions" }
    }

    fun forId(id: Int): ItemDefinition? {
        val find = definitions.find { it.id == id }
        if (find == null) logger.debug { "Unknown item definition $id" }
        return find
    }

    fun getDefinitions() = definitions
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemDefinition(
    val id: Int,
    val name: String,
    val examine: String,
    val options: Set<String>,
//    val configurations: Map<String, Object>,
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
    var tradeable: Boolean?,
    var isFullHelm: Boolean?,
    var isFullMask: Boolean?,
    var isFullBody: Boolean?,
    val lendable: Boolean,
    val highAlchemy: Int,
    val lowAlchemy: Int,
    val destroyable: Boolean,
    val weight: Double,
    val bonuses: IntArray?,
    val absorb: IntArray?,
    val isTwoHand: Boolean,
    val equipmentSlot: Int,
    var attackSpeed: Int?,
    var standAnimation: Int?,
    var standTurnAnimation: Int?,
    var walkAnimation: Int?,
    var runAnimation: Int?,
    var turn180Animation: Int?,
    var turn90CWAnimation: Int?,
    var turn90CCWAnimation: Int?,
    var weaponInterface: Int?,
    val hasSpecial: Boolean,
    val destroyMessage: String?,
    var shopPrice: Int?,
    var grandExchangePrice: Int?,
    var grandExchangeLimit: Int?,
    var defenceAnimation: Int?,
    var pointPrice: Int?,
    var tradeOverride: Boolean?,
    val tokkulPrice: Int,
    val funWeapon: Boolean,
    var renderAnimation: Int?,
    val attackAnimations: IntArray?,
    val attackSounds: IntArray?,
    val maleWornModelId1: Int,
    val unnoted: Boolean,
    var alchemizable: Boolean?,
    var defence_anim: Int?,
    var equipSound: Int?
) {

//    fun <V> getConfig(key: String, fail: V): V {
//        val value = configurations[key] as V
//        return value ?: fail
//    }

//    fun isTradable(): Boolean {
//        return tradeable && getConfig(CONFIG_TRADEABLE, false)
//    }

    @JsonIgnore
    fun isFullHelms(): Boolean {
        check(equipmentSlot == Equipment.HEAD)
        return isFullHelm!!
    }

    @JsonIgnore
    fun isFullMasks(): Boolean {
        check(equipmentSlot == Equipment.HEAD)
        return isFullMask!!
    }

    @JsonIgnore
    fun isFullBodys(): Boolean {
        check(equipmentSlot == Equipment.BODY)
        return isFullBody!!
    }

    @JsonIgnore
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

    @JsonIgnore
    fun getStance(): Int {
        if (walkAnimation == 0) return 1426
        return renderAnimation!!
    }

//    companion object {
//        val CONFIG_TRADEABLE = "tradeable"
//        val CONFIG_LENDABLE = "lendable"
//        val CONFIG_DESTROY = "destroy"
//        val CONFIG_TWO_HANDED = "two_handed"
//        val CONFIG_HIGH_ALCHEMY = "high_alchemy"
//        val CONFIG_LOW_ALCHEMY = "low_alchemy"
//        val CONFIG_SHOP_PRICE = "shop_price"
//        val CONFIG_GE_PRICE = "grand_exchange_price"
//        val CONFIG_EXAMINE = "examine"
//        val CONFIG_WEIGHT = "weight"
//        val CONFIG_BONUS = "bonuses"
//        val CONFIG_ABSORB = "absorb"
//        val CONFIG_EQUIP_SLOT = "equipment_slot"
//        val CONFIG_ATTACK_SPEED = "attack_speed"
//
//        val CONFIG_STAND_ANIM = "stand_anim" //Animation.class
//        val CONFIG_STAND_TURN_ANIM = "stand-turn_anim"
//        val CONFIG_WALK_ANIM = "walk_anim"
//        val CONFIG_RUN_ANIM = "run_anim"
//        val CONFIG_TURN180_ANIM = "turn180_anim"
//        val CONFIG_TURN90CW_ANIM = "turn90cw_anim"
//        val CONFIG_TURN90CCW_ANIM = "turn90ccw_anim"
//        val CONFIG_WEAPON_INTERFACE = "weapon_interface"
//        val CONFIG_HAS_SPECIAL = "has_special"
//        val CONFIG_ATTACK_ANIMS = "attack_anims"
//        val CONFIG_DESTROY_MESSAGE = "destroy_message"
//        val CONFIG_REQUIREMENTS = "requirements"
//        val CONFIG_GE_LIMIT = "ge_buy_limit"
//        val CONFIG_DEFENCE_ANIMATION = "defence_anim"
//        val CONFIG_ATTACK_AUDIO = "attack_audios"
//        val CONFIG_POINT_PRICE = "point_price"
//        val CONFIG_SPAWNABLE = "spawnable"
//        val CONFIG_BANKABLE = "bankable"
//        val CONFIG_RARE_ITEM = "rare_item"
//        val CONFIG_TOKKUL_PRICE = "tokkul_price"
//
//        val CONFIG_RENDER_ANIM_ID = "render_anim"
//
//        @Suppress("unused")
//        fun decode(buffer: ByteBuffer): ItemDefinition {
//
//            var groundOptions = arrayOf(null, null, "take", null, null)
//            var inventoryOptions = arrayOf(null, null, null, null, "drop")
//            while (true) {
//                val opcode = buffer.get().toInt() and 0xFF
//                if (opcode == 0) break
//                if (opcode == 1) {
//                    val inventoryModelId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 2) {
//                    val name = getJagexString(buffer)
//                } else if (opcode == 4) {
//                    val modelZoom = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 5) {
//                    val modelRotation1 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 6) {
//                    val modelRotation2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 7) {
//                    var modelOffset1 = buffer.getShort().toInt() and 0xFFFF
//                    if (modelOffset1 > 32767) modelOffset1 -= 65536
//                    modelOffset1 = modelOffset1 shl 0
//                } else if (opcode == 8) {
//                    var modelOffset2 = buffer.getShort().toInt() and 0xFFFF
//                    if (modelOffset2 > 32767) modelOffset2 -= 65536
//                    modelOffset2 = modelOffset2 shl 0
//                } else if (opcode == 11) {
//                    val isStackable = true
//                } else if (opcode == 12) {
//                    val value = buffer.getInt()
//                } else if (opcode == 16) {
//                    val isMembersOnly = true
//                } else if (opcode == 18) {
//                    val i = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 23) {
//                    val maleWearModel1 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 24) {
//                    val femaleWearModel1 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 25) {
//                    val maleWearModel2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 26) {
//                    val femaleWearModel2 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode >= 30 && opcode < 35) {
//                    groundOptions[opcode - 30] = getJagexString(buffer)
//                } else if (opcode >= 35 && opcode < 40) {
//                    inventoryOptions[opcode - 35] = getJagexString(buffer)
//                } else if (opcode == 40) {
//                    val length = buffer.get().toInt() and 0xFF
//                    var originalModelColors = ShortArray(length)
//                    var modifiedModelColors = ShortArray(length)
//                    for (index in 0..<length) {
//                        originalModelColors[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                        modifiedModelColors[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                    }
//                } else if (opcode == 41) {
//                    val length = buffer.get().toInt() and 0xFF
//                    var textureColour1 = ShortArray(length)
//                    var textureColour2 = ShortArray(length)
//                    for (index in 0..<length) {
//                        textureColour1[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                        textureColour2[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
//                    }
//                } else if (opcode == 42) {
//                    val length = buffer.get().toInt() and 0xFF
//                    for (index in 0..<length) {
//                        val i = buffer.get().toInt()
//                    }
//                } else if (opcode == 65) {
//                    val isUnnoted = true
//                } else if (opcode == 78) {
//                    val colourEquip1 = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 79) {
//                    val colourEquip2 = buffer.getShort().toInt() and 0xFFFF
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
//                    val notedId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 98) {
//                    val notedTemplateId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode >= 100 && opcode < 110) {
////                    if (def.stackableIds == null) {
//                    val stackableIds = IntArray(10)
//                    val stackableAmounts = IntArray(10)
////                    }
//                    stackableIds[opcode - 100] = buffer.getShort().toInt() and 0xFFFF
//                    stackableAmounts[opcode - 100] = buffer.getShort().toInt() and 0xFFFF
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
//                    val teamId = buffer.get().toInt() and 0xFF
//                } else if (opcode == 121) {
//                    val lendId = buffer.getShort().toInt() and 0xFFFF
//                } else if (opcode == 122) {
//                    val lendTemplateId = buffer.getShort().toInt() and 0xFFFF
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
//    }

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
    val path = "./game/src/main/resources/data"
    val file = File("$path/itemDefinitionsOLD.json")
    val map: MutableMap<Int, ItemDefinition> =
        mapper.readValue(file, object : TypeReference<MutableMap<Int, ItemDefinition>>() {})
    KotlinLogging.logger { }.info { "Loaded ${map.size} item definitions" }

    val copy = map

    //configs
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ItemConfig {
        val id: Int = 0
        val alchemizable: Boolean? = null
        val defence_anim: Int? = null
        val equip_audio: Int? = null
        val shop_price: Int? = null
        val ge_buy_limit: Int? = null
        val grand_exchange_price: Int? = null
        val tradeable: Boolean? = null

        val remove_head: Boolean? = null
        val remove_beard: Boolean? = null
        val remove_sleeves: Boolean? = null
    }

    val file2 = File("$path/item_configs.json")
    val itemConfigs: Array<ItemConfig> =
        mapper.readValue(file2, object : TypeReference<Array<ItemConfig>>() {})
    KotlinLogging.logger { }.info { "Loaded ${itemConfigs.size} item definitions" }

    //modify
    map.onEach { def ->
        copy[def.key] = def.value.also { it ->
            if (it.attackSpeed == 0) it.attackSpeed = null
            if (it.shopPrice == 0) it.shopPrice = null
            if (it.pointPrice == 0) it.pointPrice = null
            it.grandExchangeLimit = itemConfigs.find { it.id == def.key }?.ge_buy_limit
            it.grandExchangePrice = itemConfigs.find { it.id == def.key }?.grand_exchange_price
            it.alchemizable = itemConfigs.find { it.id == def.key }?.alchemizable
            it.defenceAnimation = itemConfigs.find { it.id == def.key }?.defence_anim
            it.shopPrice = itemConfigs.find { it.id == def.key }?.shop_price
            it.equipSound = itemConfigs.find { it.id == def.key }?.equip_audio
            it.tradeable = itemConfigs.find { it.id == def.key }?.tradeable

            it.isFullHelm = itemConfigs.find { it.id == def.key }?.remove_head
            it.isFullBody = itemConfigs.find { it.id == def.key }?.remove_sleeves
            it.isFullMask = itemConfigs.find { it.id == def.key }?.remove_beard
        }
    }

//write
    val writer = mapper.writer(DefaultPrettyPrinter())
    writer?.writeValue(File("$path/itemDefinitions.json"), copy.values.toTypedArray())

//    val file3 = File("$path/itemDefs-new.json")
//    val newItems: Array<ItemDefinition> = mapper.readValue(file3, object : TypeReference<Array<ItemDefinition>>() {})
//    KotlinLogging.logger { }.info { "Loaded ${newItems.size} item definitions" }

}

fun main() {
//    dump()
}

