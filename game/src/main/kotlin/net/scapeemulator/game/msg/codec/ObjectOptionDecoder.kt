package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ObjectOptionMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

private val ObjectOption1 = MessageDecoder(254) { frame ->
    val reader = GameFrameReader(frame)
    val x = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val id = reader.getSigned(DataType.SHORT, DataTransformation.ADD).toInt()
    val y = reader.getSigned(DataType.SHORT).toInt()
    return@MessageDecoder ObjectOptionMessage(1, x, y, id)
}

private val ObjectOption2 = MessageDecoder(194) { frame ->
    val reader = GameFrameReader(frame)
    val y = reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val x = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val id = reader.getSigned(DataType.SHORT).toInt()
    return@MessageDecoder ObjectOptionMessage(2, x, y, id)
}
private val ObjectOption3 = MessageDecoder(84) { frame ->
    val reader = GameFrameReader(frame)
    val id = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val y = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val x = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    return@MessageDecoder ObjectOptionMessage(3, x, y, id)
}
private val ObjectOption4 = MessageDecoder(247) { frame ->
    val reader = GameFrameReader(frame)
    val y = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val x = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val id = reader.getUnsigned(DataType.SHORT).toInt()
    return@MessageDecoder ObjectOptionMessage(4, x, y, id)
}
private val ObjectOption5 = MessageDecoder(170) { frame ->
    val reader = GameFrameReader(frame)
    val id = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val x = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val y = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder ObjectOptionMessage(5, x, y, id)
}

internal val ObjectDecoders = arrayOf(ObjectOption1, ObjectOption2, ObjectOption3, ObjectOption4, ObjectOption5)