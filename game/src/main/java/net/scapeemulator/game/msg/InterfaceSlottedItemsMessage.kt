package net.scapeemulator.game.msg

import net.scapeemulator.game.model.SlottedItem

class InterfaceSlottedItemsMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val type: Int,
    @JvmField val items: Array<SlottedItem>
) : Message()
