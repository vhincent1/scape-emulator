package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.PingMessage
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException

class PingMessageDecoder : MessageDecoder<PingMessage>(93) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): PingMessage {
        return PING_MESSAGE
    }

    companion object {
        private val PING_MESSAGE = PingMessage()
    }
}
