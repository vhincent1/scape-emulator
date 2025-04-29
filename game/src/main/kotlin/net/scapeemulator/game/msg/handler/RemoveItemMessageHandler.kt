package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Equipment
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.RemoveItemMessage

class RemoveItemMessageHandler : MessageHandler<RemoveItemMessage>() {
    override fun handle(player: Player, message: RemoveItemMessage) {
        if (message.id == Interface.EQUIPMENT && message.slot == 28) {
            val item = player.equipment.get(message.itemSlot)
            if (item == null || item.id != message.itemId) return

            Equipment.remove(player, message.itemSlot)
        }
    }
}
