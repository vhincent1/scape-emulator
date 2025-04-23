package net.scapeemulator.game.model

interface InventoryListener {
    fun itemChanged(inventory: Inventory, slot: Int, item: Item?)

    fun itemsChanged(inventory: Inventory)

    fun capacityExceeded(inventory: Inventory)
}
