package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.IdleLogoutMessage
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException

class IdleLogoutMessageDecoder : MessageDecoder<IdleLogoutMessage>(245) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): IdleLogoutMessage {
        return IDLE_LOGOUT_MESSAGE
    }

    companion object {
        private val IDLE_LOGOUT_MESSAGE = IdleLogoutMessage()
    }
}
