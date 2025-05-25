package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RunScriptMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader
import net.scapeemulator.util.Base37Utils

val EnterAmountDecoder = MessageDecoder(23) { frame ->
    val value = GameFrameReader(frame).getUnsigned(DataType.INT)
    return@MessageDecoder RunScriptMessage(value)
}

val EnterTextDecoder = MessageDecoder(244) { frame ->
    val value = GameFrameReader(frame).getSigned(DataType.LONG)
    //length < 6 crashes
    return@MessageDecoder RunScriptMessage(Base37Utils.decodeBase37(value))
}