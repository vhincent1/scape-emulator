package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ExamineMessage
import net.scapeemulator.game.msg.ExamineType
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

private val ExamineNpcDecoder = MessageDecoder(72) { frame ->
    val reader = GameFrameReader(frame)
    val id = reader.getUnsigned(DataType.SHORT).toInt()
    return@MessageDecoder ExamineMessage(id, ExamineType.NPC)
}
private val ExamineItemDecoder = MessageDecoder(92) { frame ->
    val reader = GameFrameReader(frame)
    val id = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt();
    return@MessageDecoder ExamineMessage(id, ExamineType.ITEM)
}
private val ExamineObjectDecoder = MessageDecoder(94) { frame ->
    val reader = GameFrameReader(frame)
    val id = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder ExamineMessage(id, ExamineType.OBJECT)
}

internal val ExamineDecoders = arrayOf(ExamineNpcDecoder, ExamineItemDecoder, ExamineObjectDecoder)