package net.scapeemulator.game.model

class Inventory {
    enum class StackMode {
        ALWAYS,
        STACKABLE_ONLY
    }

    private val stackMode: StackMode
    private val items: Array<Item?>
    private val listeners: MutableList<InventoryListener> = ArrayList()

    @JvmOverloads
    constructor(slots: Int, stackMode: StackMode = StackMode.STACKABLE_ONLY) {
        this.stackMode = stackMode
        this.items = arrayOfNulls(slots)
    }

    constructor(inventory: Inventory) {
        this.stackMode = inventory.stackMode
        this.items = inventory.toArray()
    }

    fun toArray(): Array<Item?> {
        val array = arrayOfNulls<Item>(items.size)
        System.arraycopy(items, 0, array, 0, items.size)
        return array
    }

    fun addListener(listener: InventoryListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: InventoryListener) {
        listeners.remove(listener)
    }

    fun removeListeners() {
        listeners.clear()
    }

    fun refresh() {
        fireItemsChanged()
    }

    fun get(slot: Int): Item? {
        checkSlot(slot)
        return items[slot]
    }

    fun set(slot: Int, item: Item?) {
        checkSlot(slot)
        items[slot] = item
        fireItemChanged(slot)
    }

    fun swap(slot1: Int, slot2: Int) {
        checkSlot(slot1)
        checkSlot(slot2)

        val tmp = items[slot1]
        items[slot1] = items[slot2]
        items[slot2] = tmp

        fireItemChanged(slot1)
        fireItemChanged(slot2)
    }

    fun reset(slot: Int) {
        set(slot, null)
    }

    //todo check slots before adding
    @JvmOverloads
    fun add(item: Item, preferredSlot: Int = -1): Item? {
        val id = item.id
        val stackable = isStackable(item)
        if (stackable) {
            /* try to add this item to an existing stack */
            var slot = slotOf(id)
            if (slot != -1) {
                val other = items[slot]
                val total = other!!.amount.toLong() + item.amount
                val amount: Int

                /* check if there are too many items in the stack */
                var remaining: Item? = null
                if (total > Int.MAX_VALUE) {
                    amount = Int.MAX_VALUE
                    remaining = Item(id, (total - amount).toInt())
                    fireCapacityExceeded()
                } else {
                    amount = total.toInt()
                }

                /* update stack and return any remaining items */
                set(slot, Item(item.id, amount))
                return remaining
            }

            /* try to add this item to the preferred slot */
            if (preferredSlot != -1) {
                checkSlot(preferredSlot)
                if (items[preferredSlot] == null) {
                    set(preferredSlot, item)
                    return null
                }
            }

            /* try to add this item to any slot */
            slot = 0
            while (slot < items.size) {
                if (items[slot] == null) {
                    set(slot, item)
                    return null
                }
                slot++
            }

            /* give up */
            fireCapacityExceeded()
            return item
        } else {
            val single = Item(id, 1)
            var remaining = item.amount

            if (remaining == 0) return null

            /* try to first place item at the preferred slot */
            if (preferredSlot != -1) {
                checkSlot(preferredSlot)
                if (items[preferredSlot] == null) {
                    set(preferredSlot, single)
                    remaining--
                }
            }

            if (remaining == 0) return null

            /* place any subsequent remaining items wherever space is available */
            for (slot in items.indices) {
                if (items[slot] == null) {
                    set(slot, single)
                    remaining--
                }

                if (remaining == 0) return null
            }

            /* give up */
            fireCapacityExceeded()
            return Item(id, remaining)
        }
    }

    @JvmOverloads
    fun remove(item: Item, preferredSlot: Int = -1): Item? {
        val id = item.id
        val stackable = isStackable(item)

        if (stackable) {
            /* try to remove this item from its stack */
            val slot = slotOf(id)
            if (slot != -1) {
                var other = items[slot]
                if (other!!.amount >= item.amount) {
                    set(slot, null)
                    return Item(id, other.amount)
                } else {
                    other = Item(id, other.amount - item.amount)
                    set(slot, other)
                    return item
                }
            }

            return null
        } else {
            var removed = 0

            /* try to remove the item from the preferred slot first */
            if (preferredSlot != -1) {
                checkSlot(preferredSlot)
                if (items[preferredSlot]!!.id == id) {
                    set(preferredSlot, null)

                    if (++removed >= item.amount) return Item(id, removed)
                }
            }

            /* try other slots */
            for (slot in items.indices) {
                val other = items[slot]
                if (other != null && other.id == id) {
                    set(slot, null)

                    if (++removed >= item.amount) return Item(id, removed)
                }
            }

            return if (removed == 0) null else Item(id, removed)
        }
    }

    fun shift() {
        var destSlot = 0

        for (slot in items.indices) {
            val item = items[slot]
            if (item != null) {
                items[destSlot++] = item
            }
        }

        for (slot in destSlot..<items.size) items[slot] = null

        fireItemsChanged()
    }

    fun empty() {
        for (slot in items.indices) items[slot] = null

        fireItemsChanged()
    }

    val isEmpty: Boolean
        get() {
            for (slot in items.indices) if (items[slot] != null) return false

            return true
        }

    fun freeSlots(): Int {
        var slots = 0
        for (slot in items.indices) if (items[slot] == null) slots++

        return slots
    }

    fun slotOf(id: Int): Int {
        for (slot in items.indices) {
            val item = items[slot]
            if (item != null && item.id == id) return slot
        }

        return -1
    }

    fun contains(id: Int): Boolean {
        return slotOf(id) != -1
    }

    private fun fireItemChanged(slot: Int) {
        for (listener in listeners)
            listener.itemChanged(this, slot, items[slot])
    }

    private fun fireItemsChanged() {
        for (listener in listeners) listener.itemsChanged(this)
    }

    fun fireCapacityExceeded() {
        for (listener in listeners) listener.capacityExceeded(this)
    }

    fun verify(slot: Int, item: Int): Boolean = get(slot) != null && get(slot)?.id == item

    private fun isStackable(item: Item): Boolean {
        if (stackMode == StackMode.ALWAYS) return true
        return item.definition?.stackable ?: false //TODO check
    }


    private fun checkSlot(slot: Int) {
        if (slot < 0 || slot >= items.size) throw IndexOutOfBoundsException("slot out of range")
    }
}
