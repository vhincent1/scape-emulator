package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.msg.ExtendedButtonMessage

internal fun ButtonMessageHandler(dispatcher: ButtonDispatcher) =
    MessageHandler<ButtonMessage> { player, message ->
        dispatcher.handle(player, message.id, message.slot, 0)
    }

internal fun ExtendedButtonMessageHandler(dispatcher: ButtonDispatcher) =
    MessageHandler<ExtendedButtonMessage> { player, message ->
        println("Extended button message: $message")
        dispatcher.handle(player, message.id, message.slot, 0)
    }