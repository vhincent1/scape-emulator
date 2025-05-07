package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.DisplayMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal val displayMessageDecoder = handleDecoder(243) { frame ->
    val reader = GameFrameReader(frame)
    val mode = reader.getUnsigned(DataType.BYTE).toInt()
    val width = reader.getUnsigned(DataType.SHORT).toInt()
    val height = reader.getUnsigned(DataType.SHORT).toInt()
    reader.getUnsigned(DataType.BYTE) // TODO identify this
    return@handleDecoder DisplayMessage(mode, width, height)
}