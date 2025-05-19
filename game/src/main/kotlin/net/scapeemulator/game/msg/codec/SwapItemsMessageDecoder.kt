package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.SwapItemsMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal val SwapItemsMessageDecoder = MessageDecoder(231) { frame ->
    val reader = GameFrameReader(frame)
    val source = reader.getUnsigned(DataType.SHORT).toInt()
    val inter = reader.getSigned(DataType.INT, DataOrder.LITTLE).toInt()
    val id = (inter shr 16) and 0xFFFF
    val slot = inter and 0xFFFF
    val destination = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    val type = reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT).toInt()
    return@MessageDecoder SwapItemsMessage(id, slot, source, destination, type)
}
