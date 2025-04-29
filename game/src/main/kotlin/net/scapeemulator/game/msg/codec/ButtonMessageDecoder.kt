package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader

class ButtonMessageDecoder : MessageDecoder<ButtonMessage>(10) {
    override fun decode(frame: GameFrame): ButtonMessage {
        val reader = GameFrameReader(frame)
        val button = reader.getSigned(DataType.INT).toInt()
        val id = (button shr 16) and 0xFFFF
        val slot = button and 0xFFFF
        return ButtonMessage(id, slot)
    }
}
