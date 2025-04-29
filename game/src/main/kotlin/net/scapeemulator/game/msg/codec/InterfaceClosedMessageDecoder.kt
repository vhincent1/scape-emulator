package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.InterfaceClosedMessage
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException

class InterfaceClosedMessageDecoder : MessageDecoder<InterfaceClosedMessage>(184) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): InterfaceClosedMessage {
        return INTERFACE_CLOSED_MESSAGE
    }

    companion object {
        private val INTERFACE_CLOSED_MESSAGE = InterfaceClosedMessage()
    }
}
