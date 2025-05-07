package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.SequenceNumberMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal val sequenceNumberMessageDecoder = handleDecoder(177) { frame ->
    val reader = GameFrameReader(frame)
    val sequenceNumber = reader.getUnsigned(DataType.SHORT).toInt()
    return@handleDecoder SequenceNumberMessage(sequenceNumber)
}