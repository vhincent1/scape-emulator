package net.scapeemulator.game.update

import net.scapeemulator.game.model.*
import net.scapeemulator.game.model.EquipmentDefinition.Companion.forId
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder
import net.scapeemulator.util.Base37Utils

class AppearancePlayerBlock(player: Player) : PlayerBlock(0x4) {
    private val username: String = player.username
    private val appearance: Appearance = player.appearance
    private val equipment: Inventory = Inventory(player.equipment)
    private val stance: Int = player.stance
    private val combat: Int = player.skillSet.combatLevel
    private val skill: Int = player.skillSet.totalLevel

    public override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        val gender = appearance.gender

        val propertiesBuilder = GameFrameBuilder(builder.allocator)

        /*
		 * flags field:
		 *   bit 0   - gender (0 = male, 1 = female)
		 *   bit 1   - unused
		 *   bit 2   - show skill level instead of combat level
		 *   bit 3-5 - unknown
		 *   bit 6-7 - unknown
		 */
        val flags = gender.ordinal
        propertiesBuilder.put(DataType.BYTE, flags)
        propertiesBuilder.put(DataType.BYTE, -1) // pk icon
        propertiesBuilder.put(DataType.BYTE, -1) // prayer icon

        var item = equipment.get(Equipment.HEAD)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        item = equipment.get(Equipment.CAPE)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        item = equipment.get(Equipment.NECK)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        item = equipment.get(Equipment.WEAPON)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        item = equipment.get(Equipment.BODY)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.SHORT, 0x100 or appearance.style[2])
        }

        item = equipment.get(Equipment.SHIELD)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        var fullBody = false
        item = equipment.get(Equipment.BODY)
        if (item != null) fullBody = forId(item.id)!!.isFullBody()

        if (!fullBody) {
            propertiesBuilder.put(DataType.SHORT, 0x100 or appearance.style[3])
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        item = equipment.get(Equipment.LEGS)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.SHORT, 0x100 or appearance.style[5])
        }

        var fullHelm = false
        var fullMask = false
        item = equipment.get(Equipment.HEAD)
        if (item != null) {
            fullHelm = forId(item.id)!!.isFullHelm()
            fullMask = forId(item.id)!!.isFullMask()
        }
        if (!fullHelm && !fullMask) {
            propertiesBuilder.put(DataType.SHORT, 0x100 or appearance.style[0])
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        item = equipment.get(Equipment.HANDS)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.SHORT, 0x100 or appearance.style[4])
        }

        item = equipment.get(Equipment.FEET)
        if (item != null) {
            propertiesBuilder.put(DataType.SHORT, 0x8000 or forId(item.id)!!.equipmentId)
        } else {
            propertiesBuilder.put(DataType.SHORT, 0x100 or appearance.style[6])
        }

        item = equipment.get(Equipment.HEAD) // TODO check
        if (gender == Gender.MALE && !fullMask && !fullHelm) {
            propertiesBuilder.put(DataType.SHORT, 0x100 or appearance.style[1])
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
        }

        for (i in 0..4) {
            propertiesBuilder.put(DataType.BYTE, appearance.colors[i])
        }

        propertiesBuilder.put(DataType.SHORT, stance)
        propertiesBuilder.put(DataType.LONG, Base37Utils.encodeBase37(username!!))
        propertiesBuilder.put(DataType.BYTE, combat)
        if ((flags and 0x4) != 0) {
            propertiesBuilder.put(DataType.SHORT, skill)
        } else {
            propertiesBuilder.put(DataType.BYTE, 0)
            propertiesBuilder.put(DataType.BYTE, 0)
        }
        propertiesBuilder.put(DataType.BYTE, 0)

        /* if the above byte is non-zero, four unknown shorts are written */
        builder.put(DataType.BYTE, DataTransformation.ADD, propertiesBuilder.length)
        builder.putRawBuilder(propertiesBuilder)
    }
}
