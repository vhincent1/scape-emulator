package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.PingMessage

private val PING_MESSAGE = PingMessage()
internal val pingMessageDecoder = handleDecoder(93) { frame ->
    return@handleDecoder PING_MESSAGE
}