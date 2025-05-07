package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ChatMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader
import net.scapeemulator.util.ChatUtils

internal val chatMessageDecoder = handleDecoder(237) { frame ->
    val reader = GameFrameReader(frame)
    val size = reader.length - 2

    val color = reader.getUnsigned(DataType.BYTE).toInt()
    val effects = reader.getUnsigned(DataType.BYTE).toInt()

    val bytes = ByteArray(size)
    reader.getBytes(bytes)
    val text = ChatUtils.unpack(bytes)

    return@handleDecoder ChatMessage(color, effects, text)
}