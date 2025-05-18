package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.msg.ExtendedButtonMessage

internal fun buttonMessageHandler(dispatcher: ButtonDispatcher) = messageHandler<ButtonMessage> { player, message ->
    dispatcher.handle(player, message.id, message.slot, 0)
}

internal fun extendedButtonMessageHandler(dispatcher: ButtonDispatcher) =
    messageHandler<ExtendedButtonMessage> { player, message ->
        println("Extended button message: $message")
        dispatcher.handle(player, message.id, message.slot, message.parameter)
    }