package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.WalkMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal fun walkMessageDecoder(opcode: Int) = MessageDecoder(opcode) { frame ->
    val reader = GameFrameReader(frame)
    val anticheat = frame.opcode == 39
    val stepCount = (reader.length - (if (anticheat) 19 else 5)) / 2 + 1
//    val minimapFlagId = reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT).toInt()
    val running = reader.getUnsigned(DataType.BYTE, DataTransformation.ADD) == 1L
    val x = reader.getUnsigned(DataType.SHORT).toInt()
    val y = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()

    val steps = arrayOfNulls<WalkMessage.Step>(stepCount)
    steps[0] = WalkMessage.Step(x, y)
    for (i in 1..<stepCount) {
        val stepX = x + reader.getSigned(DataType.BYTE, DataTransformation.ADD).toInt()
        val stepY = y + reader.getSigned(DataType.BYTE, DataTransformation.SUBTRACT).toInt()
        steps[i] = WalkMessage.Step(stepX, stepY)
    }
    return@MessageDecoder WalkMessage(WalkMessage.Step(x, y), steps, running)
}