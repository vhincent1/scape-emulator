package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.FlagsMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import java.io.IOException

class FlagsMessageDecoder : MessageDecoder<FlagsMessage>(98) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): FlagsMessage {
        val reader = GameFrameReader(frame)
        val flags = reader.getUnsigned(DataType.INT).toInt()
        return FlagsMessage(flags)
    }
}
