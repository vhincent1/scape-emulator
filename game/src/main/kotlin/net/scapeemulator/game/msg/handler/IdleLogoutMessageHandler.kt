package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.IdleLogoutMessage

class IdleLogoutMessageHandler : MessageHandler<IdleLogoutMessage>() {
    override fun handle(player: Player, message: IdleLogoutMessage) {
        // TODO implement
    }
}
