package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.FlagsMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal val FlagsMessageDecoder = MessageDecoder(98) { frame ->
    val reader = GameFrameReader(frame)
    val flags = reader.getUnsigned(DataType.INT).toInt()
    return@MessageDecoder FlagsMessage(flags)
}
