package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.util.sendGroundItem

//class GroundItem(override var position: Position, val item: Item, val owner: String) : Entity()

class GroundItem(id: Int, amount: Int, var position: Position, var owner: Player? = null) : Item(id, amount) {
    var private = false
    var expire: Int = 0
    var removed = false

    init {
        resetTimer()
    }


//    override var position: Position
//        get() = super.position
//        set(position) {
//            // Don't allow modification of the position
//        }


    fun resetTimer() {
        expire = if (private) 100 else 200
    }

}

/**
 * @author Hadyn Richard
 * @author David Insley
 */
//class GroundItemList {
//    /**
//     * The ground item stacks in this list.
//     */
//    private val stacks: MutableMap<Position, GroundItemStack> = LinkedHashMap()
//
//    /**
//     * The list of listeners for this list.
//     */
//    private val listeners: MutableList<GroundItemListener> = LinkedList<GroundItemListener>()
//
//    /**
//     * The class that represents a stack of ground items at a specific location.
//     */
//    private inner class GroundItemStack
//    /**
//     * Constructs a new [GroundItemStack];
//     *
//     * @param position The position of the stack.
//     */(
//        /**
//         * The position at which the stack is located at.
//         */
//        private val position: Position
//    ) {
//        /**
//         * The ground items that are contained in the stack.
//         */
//        private val groundItems: MutableList<GroundItem> = LinkedList()
//
//        /**
//         * Appends a new ground item to the stack.
//         *
//         * @param itemId The id of the item to append.
//         * @param amount The amount of the item.
//         * @return the GroundItem modified or created
//         */
//        fun add(itemId: Int, amount: Int, owner: Int): GroundItem {
//            if (ItemDefinitions.forId(itemId)?.stackable!!) {
//                for (groundItem in groundItems) {
//                    if (groundItem.itemId != itemId || groundItem.owner != owner) {
//                        continue
//                    }
//                    if (BasicMath.integerOverflow(groundItem.getAmount(), amount) != 0) {
//                        continue
//                    }
//                    /* update the ground item */
//                    groundItem.setAmount(groundItem.getAmount() + amount)
//                    groundItem.resetTimer()
//                    return groundItem
//                }
//            }
//
//            /* add the ground item to the ground item list */
//            val groundItem = GroundItem(counter.getAndIncrement(), position, itemId, amount, owner)
//            groundItems.add(groundItem)
//
//            /* alert the listeners a new ground item was created */
//            for (listener in listeners) {
//                if (listener.shouldFireEvents(groundItem)) {
//                    listener.groundItemCreated(groundItem)
//                }
//            }
//            return groundItem
//        }
//
//        /**
//         * Gets the first ground item in the list with the specified item id that is visible to the
//         * specified owner.
//         *
//         * @param itemId The item id for the ground item to get.
//         * @return The found ground item.
//         */
//        fun get(itemId: Int, owner: Int): GroundItem? {
//            for (groundItem in groundItems) {
//                if (groundItem.itemId != itemId) {
//                    continue
//                }
//                if (groundItem.owner != PUBLIC_ITEM && groundItem.owner != owner) {
//                    continue
//                }
//                return groundItem
//            }
//            return null
//        }
//
//        /**
//         * Removes the first ground item in the stack with the given item id that is visible to the
//         * specified owner.
//         *
//         * @param itemId the item id to check for
//         * @param owner the owner to check visibility for
//         * @return the GroundItem that was removed
//         */
//        fun remove(itemId: Int, owner: Int): GroundItem? {
//            val groundItem = get(itemId, owner)
//            if (groundItem != null) {
//                groundItems.remove(groundItem)
//                for (listener in listeners) {
//                    if (listener.shouldFireEvents(groundItem)) {
//                        listener.groundItemRemoved(groundItem)
//                    }
//                }
//            }
//            return groundItem
//        }
//
//        /**
//         * Gets if the stack contains a ground item with the specified item id visible to the
//         * specified player.
//         *
//         * @param player the player to check visibility for
//         * @param itemId the item id for the ground item to check
//         * @return true if the ground item with the item id exists
//         */
//        fun contains(itemId: Int, owner: Int): Boolean {
//            return get(itemId, owner) != null
//        }
//
//        val isEmpty: Boolean
//            /**
//             * Gets if the stack is empty.
//             *
//             * @return If the ground item map is empty.
//             */
//            get() = groundItems.isEmpty()
//
//        /**
//         * Gets the ground items in the stack.
//         *
//         * @return the ground item list
//         */
//        fun getGroundItems(): List<GroundItem> {
//            return groundItems
//        }
//    }
//
//    /**
//     * Adds a listener for the list.
//     */
//    fun addListener(listener: GroundItemListener) {
//        listeners.add(listener)
//    }
//
//    /**
//     * Removes a listener from the list.
//     */
//    fun removeListener(listener: GroundItemListener) {
//        listeners.remove(listener)
//    }
//
//    /**
//     * Fires a ground item created event for each ground item in the list.
//     */
//    fun fireEvents(listener: GroundItemListener) {
//        for ((_, value) in stacks) {
//            for (groundItem in value.getGroundItems()) {
//                if (listener.shouldFireEvents(groundItem)) {
//                    listener.groundItemCreated(groundItem)
//                }
//            }
//        }
//    }
//
//    /**
//     * Adds a ground item to the list.
//     *
//     * @param itemId The id of the item.
//     * @param amount The amount of the item.
//     * @param position The position of the ground item.
//     * @return
//     */
//    fun add(itemId: Int, amount: Int, position: Position, player: Player?): GroundItem {
//        var stack = stacks[position]
//        if (stack == null) {
//            stack = GroundItemStack(position)
//            stacks[position] = stack
//        }
//        return stack.add(itemId, amount, player?.databaseId ?: PUBLIC_ITEM)
//    }
//
//    /**
//     * Gets the first ground item at the given position that has the given item id and is visible to
//     * the specified player.
//     *
//     * @param itemId The item id.
//     * @param position The position of the item.
//     * @return The ground item.
//     */
//    fun get(itemId: Int, position: Position, player: Player?): GroundItem? {
//        val stack = stacks[position] ?: return null
//        return stack.get(itemId, player?.databaseId ?: PUBLIC_ITEM)
//    }
//
//    /**
//     * Checks if the list contains a ground item visible to the player.
//     *
//     * @param player the player to check visibility for
//     * @param itemId the id of the item to check for
//     * @param position the position of the item
//     * @return If the ground item exists.
//     */
//    fun contains(itemId: Int, position: Position, player: Player?): Boolean {
//        val stack = stacks[position] ?: return false
//        return stack.contains(itemId, player?.databaseId ?: PUBLIC_ITEM)
//    }
//
//    /**
//     * Removes the first ground item at the given position that has the given item id and is visible
//     * to the specified player.
//     *
//     * @param itemId
//     * @param position
//     * @param player
//     * @return the ground item that was removed
//     */
//    fun remove(itemId: Int, position: Position, player: Player?): GroundItem? {
//        val stack = stacks[position] ?: return null
//        val removed = stack.remove(itemId, player?.databaseId ?: PUBLIC_ITEM)
//        if (stack.isEmpty) {
//            stacks.remove(position)
//        }
//        return removed
//    }
//
//    fun tick() {
//        val toRemove: MutableList<GroundItem> = LinkedList()
//        for (stack in stacks.values) {
//            for (groundItem in stack.getGroundItems()) {
//                groundItem.decrementTimer()
//                if (groundItem.timeLeft < 1) {
//                    toRemove.add(groundItem)
//                }
//            }
//        }
//        for (groundItem in toRemove) {
//            if (groundItem.owner == PUBLIC_ITEM) {
//                /*
//                 * Because the remove ground item packet will remove the first item in the clients
//                 * deque that matches the id and position, it is impossible to remove an item below
//                 * another with the same id. TODO - Fix this somehow, in the meantime there may be
//                 * visual inconsistencies if two stackable items with the same ID are dropped and
//                 * the public item expires first
//                 */
//                val removed = remove(groundItem.itemId, groundItem.position, null)
//            } else {
//                groundItem.owner = PUBLIC_ITEM
//                groundItem.resetTimer()
//                for (listener in listeners) {
//                    if (listener.shouldFireEvents(groundItem)) {
//                        listener.groundItemCreated(groundItem)
//                    }
//                }
//            }
//        }
//    }
//
//    companion object {
//        /**
//         * The owner identifier for a public item.
//         */
//        const val PUBLIC_ITEM: Int = -1
//
//        /**
//         * The owner identifier for a public item that doesn't expire after a timer. TODO
//         */
//        const val PUBLIC_ITEM_PERSIST: Int = -2
//
//        /**
//         * The UID counter for all the ground items that are created.
//         */
//        private val counter = AtomicInteger(1)
//    }
//}

class GroundItems {

    val ground_items = NodeList<GroundItem>(World.MAX_GROUND_ITEMS)

    companion object {
    }

    fun tick() = ground_items.tick()

    fun create(groundItem: GroundItem) {
        if (groundItem.definition?.tradeable!!) groundItem.private = true
        //todo item plugins
        if (ground_items.add(groundItem)) return
        //item lookup

        if (!groundItem.private) {
            GameServer.INSTANCE.world.players.forEach { p ->
                p?.sendGroundItem(groundItem)
            }
        } else {
            groundItem.owner?.sendGroundItem(groundItem)
        }
    }

    private fun NodeList<GroundItem>.tick() {
        for (item in this) {
            if (item != null) item.expire--
        }
    }

}

abstract class GroundItemListener {
    abstract fun shouldFireEvents(groundItem: GroundItem): Boolean

    abstract fun groundItemCreated(groundItem: GroundItem)

    abstract fun groundItemUpdated(groundItem: GroundItem, previousAmount: Int)

    abstract fun groundItemRemoved(groundItem: GroundItem)
}