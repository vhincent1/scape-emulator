package net.scapeemulator.game.model

class InventoryAppearanceListener(private val player: Player) : InventoryListener {
    override fun itemChanged(inventory: Inventory, slot: Int, item: Item?) {
        player.appearance = player.appearance
    }

    override fun itemsChanged(inventory: Inventory) {
        player.appearance = player.appearance
    }

    override fun capacityExceeded(inventory: Inventory) {
        /* empty */
    }
}
