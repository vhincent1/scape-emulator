package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RunScriptMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader
import net.scapeemulator.util.Base37Utils

val enterAmountDecoder = handleDecoder(23) { frame ->
    val value = GameFrameReader(frame).getUnsigned(DataType.INT)
    return@handleDecoder RunScriptMessage(value)
}

val enterTextDecoder = handleDecoder(244) { frame ->
    val value = GameFrameReader(frame).getSigned(DataType.LONG)
    return@handleDecoder RunScriptMessage(Base37Utils.decodeBase37(value))
}

