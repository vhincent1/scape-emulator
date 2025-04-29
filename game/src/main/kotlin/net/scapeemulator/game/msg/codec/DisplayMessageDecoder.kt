package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.DisplayMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import java.io.IOException

class DisplayMessageDecoder : MessageDecoder<DisplayMessage>(243) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): DisplayMessage {
        val reader = GameFrameReader(frame)
        val mode = reader.getUnsigned(DataType.BYTE).toInt()
        val width = reader.getUnsigned(DataType.SHORT).toInt()
        val height = reader.getUnsigned(DataType.SHORT).toInt()
        reader.getUnsigned(DataType.BYTE) // TODO identify this
        return DisplayMessage(mode, width, height)
    }
}
