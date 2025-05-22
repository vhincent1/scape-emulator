package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.SlottedItem

data class InterfaceVisibleMessage(@JvmField val id: Int, @JvmField val slot: Int, @JvmField val isVisible: Boolean) :
    Message()

class InterfaceClosedMessage : Message()
data class InterfaceCloseMessage(@JvmField val id: Int, @JvmField val slot: Int) : Message()

//type = isWalkable
data class InterfaceOpenMessage(
    @JvmField val id: Int, @JvmField val slot: Int,
    @JvmField val childId: Int,
    @JvmField val type: Int
) : Message()

data class InterfaceResetItemsMessage(@JvmField val id: Int, @JvmField val slot: Int) : Message()
data class InterfaceItemsMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val type: Int,
    @JvmField val items: Array<Item?>
) : Message()

data class InterfaceRootMessage(@JvmField val id: Int, @JvmField val type: Int) : Message()
data class InterfaceSlottedItemsMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val type: Int,
    @JvmField val items: Array<SlottedItem>
) : Message()

data class InterfaceTextMessage(@JvmField val id: Int, @JvmField val slot: Int, @JvmField val text: String) : Message()
data class AnimateInterfaceMessage(
    @JvmField val animationId: Int, @JvmField val interfaceId: Int, @JvmField val childId: Int
) : Message()