package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.ButtonMessage

class ButtonMessageHandler(private val dispatcher: ButtonDispatcher) :
    MessageHandler<ButtonMessage>() {
    override fun handle(player: Player, message: ButtonMessage) {
        dispatcher.handle(player, message.id, message.slot, 0)
    }
}
