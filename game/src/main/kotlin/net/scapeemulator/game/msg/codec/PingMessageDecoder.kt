package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.PingMessage

private val PING_MESSAGE = PingMessage()
internal val PingMessageDecoder = MessageDecoder(93) { frame ->
    return@MessageDecoder PING_MESSAGE
}