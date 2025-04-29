package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.SequenceNumberMessage

class SequenceNumberMessageHandler : MessageHandler<SequenceNumberMessage>() {
    override fun handle(player: Player, message: SequenceNumberMessage) {
        // TODO implement
    }
}
