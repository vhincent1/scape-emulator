package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.InterfaceClosedMessage

private val INTERFACE_CLOSED_MESSAGE = InterfaceClosedMessage()
val InterfaceClosedMessageDecoder = MessageDecoder(184) {
    return@MessageDecoder INTERFACE_CLOSED_MESSAGE
}