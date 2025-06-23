package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RunScriptMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

val EnterAmountDecoder = MessageDecoder(23) { frame ->
    val value = GameFrameReader(frame).getUnsigned(DataType.INT)
    return@MessageDecoder RunScriptMessage(value)
}

val EnterTextDecoder = MessageDecoder(244) { frame ->
    val value = GameFrameReader(frame).getUnsigned(DataType.LONG)
    return@MessageDecoder RunScriptMessage(value)
}