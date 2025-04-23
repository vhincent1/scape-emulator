package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.SequenceNumberMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import java.io.IOException

class SequenceNumberMessageDecoder : MessageDecoder<SequenceNumberMessage>(177) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): SequenceNumberMessage {
        val reader = GameFrameReader(frame)
        val sequenceNumber = reader.getUnsigned(DataType.SHORT).toInt()
        return SequenceNumberMessage(sequenceNumber)
    }
}
