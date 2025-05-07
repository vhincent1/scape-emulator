package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.IdleLogoutMessage

private val IDLE_LOGOUT_MESSAGE = IdleLogoutMessage()
internal val idleLogoutMessageDecoder = handleDecoder(245) { frame ->
    return@handleDecoder IDLE_LOGOUT_MESSAGE
}
