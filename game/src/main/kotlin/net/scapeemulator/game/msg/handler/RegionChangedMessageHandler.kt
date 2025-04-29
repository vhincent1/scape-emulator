package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.RegionChangedMessage

class RegionChangedMessageHandler : MessageHandler<RegionChangedMessage>() {
    override fun handle(player: Player, message: RegionChangedMessage) {
        // TODO implement
    }
}
