package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.SwapItemsMessage
import net.scapeemulator.game.net.game.*
import java.io.IOException

class SwapItemsMessageDecoder : MessageDecoder<SwapItemsMessage>(231) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): SwapItemsMessage {
        val reader = GameFrameReader(frame)
        val source = reader.getUnsigned(DataType.SHORT).toInt()
        val inter = reader.getSigned(DataType.INT, DataOrder.LITTLE).toInt()
        val id = (inter shr 16) and 0xFFFF
        val slot = inter and 0xFFFF
        val destination = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
        val type = reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT).toInt()
        return SwapItemsMessage(id, slot, source, destination, type)
    }
}
