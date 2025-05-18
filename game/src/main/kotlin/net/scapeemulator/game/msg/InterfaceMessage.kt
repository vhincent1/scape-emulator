package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.SlottedItem

class InterfaceVisibleMessage(@JvmField val id: Int, @JvmField val slot: Int, @JvmField val isVisible: Boolean) :
    Message()

class InterfaceClosedMessage : Message()
class InterfaceCloseMessage(@JvmField val id: Int, @JvmField val slot: Int) : Message()

//type = isWalkable
class InterfaceOpenMessage(
    @JvmField val id: Int, @JvmField val slot: Int,
    @JvmField val childId: Int,
    @JvmField val type: Int
) : Message()

class InterfaceResetItemsMessage(@JvmField val id: Int, @JvmField val slot: Int) : Message()
class InterfaceItemsMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val type: Int,
    @JvmField val items: Array<Item?>
) : Message()

class InterfaceRootMessage(@JvmField val id: Int, @JvmField val type: Int) : Message()
class InterfaceSlottedItemsMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val type: Int,
    @JvmField val items: Array<SlottedItem>
) : Message()

class InterfaceTextMessage(@JvmField val id: Int, @JvmField val slot: Int, @JvmField val text: String) : Message()
class AnimateInterfaceMessage(
    @JvmField val animationId: Int, @JvmField val interfaceId: Int, @JvmField val childId: Int
) : Message()