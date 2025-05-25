package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.SlottedItem

data class InterfaceVisibleMessage(val id: Int, val slot: Int, val isVisible: Boolean) :
    Message()

class InterfaceClosedMessage : Message()
data class InterfaceCloseMessage(val id: Int, val slot: Int) : Message()

//type = isWalkable
data class InterfaceOpenMessage(
    val id: Int, val slot: Int, val childId: Int, val type: Int
) : Message()

data class InterfaceResetItemsMessage(val id: Int, val slot: Int) : Message()
class InterfaceItemsMessage(
    val id: Int, val slot: Int, val type: Int, val items: Array<Item?>
) : Message()

data class InterfaceRootMessage(val id: Int, val type: Int) : Message()
class InterfaceSlottedItemsMessage(
    val id: Int, val slot: Int, val type: Int, val items: Array<SlottedItem>
) : Message()

data class InterfaceTextMessage(val id: Int, val slot: Int, val text: String) : Message()
data class AnimateInterfaceMessage(
    val animationId: Int, val interfaceId: Int, val childId: Int
) : Message()