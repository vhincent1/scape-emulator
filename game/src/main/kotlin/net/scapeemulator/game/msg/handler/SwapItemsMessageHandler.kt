package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.msg.SwapItemsMessage

internal val swapItemsMessageHandler = messageHandler<SwapItemsMessage> { player, message ->
    if (message.id == Interface.INVENTORY && message.slot == 0) {
        player.inventory.swap(message.source, message.destination)
    }
}