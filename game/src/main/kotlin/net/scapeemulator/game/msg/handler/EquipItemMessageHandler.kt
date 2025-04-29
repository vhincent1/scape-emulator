package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Equipment
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.EquipItemMessage

class EquipItemMessageHandler : MessageHandler<EquipItemMessage>() {
    override fun handle(player: Player, message: EquipItemMessage) {
        if (message.id == Interface.INVENTORY && message.slot == 0) {
            val item = player.inventory.get(message.itemSlot)
            if (item == null || item.id != message.itemId) return

            Equipment.equip(player, message.itemSlot)
        }
    }
}

