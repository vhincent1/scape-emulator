package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.ExtendedButtonMessage

class ExtendedButtonMessageHandler(private val dispatcher: ButtonDispatcher) : MessageHandler<ExtendedButtonMessage>() {
    override fun handle(player: Player, message: ExtendedButtonMessage) {
        dispatcher.handle(player, message.id, message.slot, message.parameter)
    }
}
