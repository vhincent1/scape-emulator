package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.FocusMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

private val FOCUSED_MESSAGE = FocusMessage(true)
private val NOT_FOCUSED_MESSAGE = FocusMessage(false)
internal val focusMessageDecoder = handleDecoder(22) { frame ->
    val reader = GameFrameReader(frame)
    val focused = reader.getUnsigned(DataType.BYTE).toInt()
    return@handleDecoder if (focused != 0) FOCUSED_MESSAGE else NOT_FOCUSED_MESSAGE
}