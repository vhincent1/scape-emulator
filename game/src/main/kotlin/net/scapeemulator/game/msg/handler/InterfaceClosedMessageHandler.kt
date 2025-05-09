package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.msg.InterfaceClosedMessage

internal val interfaceClosedMessageHandler = messageHandler<InterfaceClosedMessage> { player, message ->
    println("Closed interface $message")
//todo impl
}
