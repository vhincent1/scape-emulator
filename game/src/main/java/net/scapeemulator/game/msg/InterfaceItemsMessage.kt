package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Item

class InterfaceItemsMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val type: Int,
    @JvmField val items: Array<Item?>
) :
    Message()
