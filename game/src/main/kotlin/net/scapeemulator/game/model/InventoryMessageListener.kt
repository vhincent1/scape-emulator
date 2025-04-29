package net.scapeemulator.game.model

import net.scapeemulator.game.msg.InterfaceItemsMessage
import net.scapeemulator.game.msg.InterfaceResetItemsMessage
import net.scapeemulator.game.msg.InterfaceSlottedItemsMessage

class InventoryMessageListener(
    private val player: Player,
    private val id: Int,
    private val slot: Int,
    private val type: Int
) : InventoryListener {
    override fun itemChanged(inventory: Inventory, slot: Int, item: Item?) {
        val items: Array<SlottedItem> = arrayOf(SlottedItem(slot, item))
        player.send(InterfaceSlottedItemsMessage(id, this.slot, type, items))
    }

    override fun itemsChanged(inventory: Inventory) {
        if (inventory.isEmpty) {
            // TODO: consider how this interacts with the 'type'?
            player.send(InterfaceResetItemsMessage(id, slot))
        } else {
            val items = inventory.toArray()
            player.send(InterfaceItemsMessage(id, slot, type, items))
        }
    }

    override fun capacityExceeded(inventory: Inventory) {
        /* empty */
    }
}
