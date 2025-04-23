package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RemoveItemMessage
import net.scapeemulator.game.net.game.*
import java.io.IOException

class RemoveItemMessageDecoder : MessageDecoder<RemoveItemMessage>(81) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): RemoveItemMessage {
        val reader = GameFrameReader(frame)
        val itemSlot = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
        val itemId = reader.getUnsigned(DataType.SHORT).toInt()
        val inter = reader.getSigned(DataType.INT, DataOrder.MIDDLE).toInt()
        val id = (inter shr 16) and 0xFFFF
        val slot = inter and 0xFFFF
        return RemoveItemMessage(id, slot, itemSlot, itemId)
    }
}
