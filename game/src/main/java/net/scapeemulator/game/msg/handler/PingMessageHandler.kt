package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PingMessage

class PingMessageHandler : MessageHandler<PingMessage>() {
    override fun handle(player: Player, message: PingMessage) {
        // TODO implement
    }
}
