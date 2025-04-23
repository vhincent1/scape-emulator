package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ClickMessage
import net.scapeemulator.game.net.game.*
import java.io.IOException

class ClickMessageDecoder : MessageDecoder<ClickMessage>(75) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): ClickMessage {
        val reader = GameFrameReader(frame)
        val flags = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
        val pos = reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE).toInt()

        val time = flags and 0x7fff
        val rightClick = ((flags shr 15) and 0x1) != 0

        val x = pos and 0xffff
        val y = (pos shr 16) and 0xffff

        return ClickMessage(time, x, y, rightClick)
    }
}
