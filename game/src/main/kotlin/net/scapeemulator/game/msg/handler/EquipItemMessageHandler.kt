package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Equipment
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.msg.EquipItemMessage

internal val equipItemMessageHandler = messageHandler<EquipItemMessage> { player, message ->
    if (message.id == Interface.INVENTORY && message.slot == 0) {
        val item = player.inventory.get(message.itemSlot)
        if (item == null || item.id != message.itemId) return@messageHandler

        Equipment.equip(player, message.itemSlot)
    }
}

