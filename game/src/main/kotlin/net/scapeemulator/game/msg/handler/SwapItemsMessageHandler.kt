package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.SwapItemsMessage

class SwapItemsMessageHandler : MessageHandler<SwapItemsMessage>() {
    override fun handle(player: Player, message: SwapItemsMessage) {
        if (message.id == Interface.INVENTORY && message.slot == 0) {
            player.inventory.swap(message.source, message.destination)
        }
    }
}
