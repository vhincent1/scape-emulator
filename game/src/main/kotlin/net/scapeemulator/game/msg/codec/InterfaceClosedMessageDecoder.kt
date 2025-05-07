package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.InterfaceClosedMessage

private val INTERFACE_CLOSED_MESSAGE = InterfaceClosedMessage()
val interfaceClosedMessageDecoder = handleDecoder(184) {
    return@handleDecoder INTERFACE_CLOSED_MESSAGE
}