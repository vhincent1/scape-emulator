package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ObjectOptionOneMessage
import net.scapeemulator.game.msg.ObjectOptionTwoMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal val ObjectOptionOneMessageDecoder = MessageDecoder(254) { frame ->
    val reader = GameFrameReader(frame)
    val x = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val id = reader.getSigned(DataType.SHORT, DataTransformation.ADD).toInt()
    val y = reader.getSigned(DataType.SHORT).toInt()
    return@MessageDecoder ObjectOptionOneMessage(x, y, id)
}

internal val ObjectOptionTwoMessageDecoder = MessageDecoder(194) { frame ->
    val reader = GameFrameReader(frame)
    val y = reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val x = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val id = reader.getSigned(DataType.SHORT).toInt()
    return@MessageDecoder ObjectOptionTwoMessage(x, y, id)
}