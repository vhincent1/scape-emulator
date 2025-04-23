package net.scapeemulator.game.model

class InventoryFullListener(private val player: Player, private val name: String) : InventoryListener {
    override fun itemChanged(inventory: Inventory, slot: Int, item: Item?) {
        /* ignore */
    }

    override fun itemsChanged(inventory: Inventory) {
        /* ignore */
    }

    override fun capacityExceeded(inventory: Inventory) {
        player.sendMessage("Not enough " + name + " space.")
    }
}
