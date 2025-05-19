package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.FocusMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

private val FOCUSED_MESSAGE = FocusMessage(true)
private val NOT_FOCUSED_MESSAGE = FocusMessage(false)
internal val FocusMessageDecoder = MessageDecoder(22) { frame ->
    val reader = GameFrameReader(frame)
    val focused = reader.getUnsigned(DataType.BYTE).toInt()
    return@MessageDecoder if (focused != 0) FOCUSED_MESSAGE else NOT_FOCUSED_MESSAGE
}