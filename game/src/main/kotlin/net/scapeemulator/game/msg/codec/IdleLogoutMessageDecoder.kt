package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.IdleLogoutMessage

private val IDLE_LOGOUT_MESSAGE = IdleLogoutMessage()
internal val IdleLogoutMessageDecoder = MessageDecoder(245) { frame ->
    return@MessageDecoder IDLE_LOGOUT_MESSAGE
}
