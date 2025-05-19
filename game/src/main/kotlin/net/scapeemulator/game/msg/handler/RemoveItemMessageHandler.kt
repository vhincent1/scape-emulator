package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Equipment
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.msg.RemoveItemMessage

internal val RemoveItemMessageHandler = MessageHandler<RemoveItemMessage> { player, message ->
    if (message.id == Interface.EQUIPMENT && message.slot == 28) {
        val item = player.equipment.get(message.itemSlot)
        if (item == null || item.id != message.itemId) return@MessageHandler

        Equipment.remove(player, message.itemSlot)
    }
}