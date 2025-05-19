package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.EquipItemMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal val EquipItemMessageDecoder = MessageDecoder(55) { frame ->
    val reader = GameFrameReader(frame)
    val itemId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val itemSlot = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    val inter = reader.getSigned(DataType.INT, DataOrder.MIDDLE).toInt()
    val id = (inter shr 16) and 0xFFFF
    val slot = inter and 0xFFFF
    return@MessageDecoder EquipItemMessage(id, slot, itemSlot, itemId)
}