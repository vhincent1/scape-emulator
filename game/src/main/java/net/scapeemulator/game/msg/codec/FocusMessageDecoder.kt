package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.FocusMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import java.io.IOException

class FocusMessageDecoder : MessageDecoder<FocusMessage>(22) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): FocusMessage {
        val reader = GameFrameReader(frame)
        val focused = reader.getUnsigned(DataType.BYTE).toInt()
        return if (focused != 0) FOCUSED_MESSAGE else NOT_FOCUSED_MESSAGE
    }

    companion object {
        private val FOCUSED_MESSAGE = FocusMessage(true)
        private val NOT_FOCUSED_MESSAGE = FocusMessage(false)
    }
}
