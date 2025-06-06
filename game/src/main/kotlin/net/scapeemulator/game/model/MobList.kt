package net.scapeemulator.game.model

//typealias PlayerList = MobList<Player>
//typealias NpcList = MobList<Player>

//class ActorList<T : Mob>(capacity: Int) : Iterable<T> {
//    private val mobs = arrayOfNulls<Mob>(capacity)
//    var size = 0
//
//    private inner class MobListIterator : MutableIterator<T> {
//        private var index = 0
//
//        override fun hasNext(): Boolean {
//            for (i in index..<mobs.size) {
//                if (mobs[i] != null) return true
//            }
//            return false
//        }
//
//        override fun next(): T {
//            while (index < mobs.size) {
//                if (mobs[index] != null) return mobs[index++] as T
//                index++
//            }
//            throw NoSuchElementException()
//        }
//
//        override fun remove() {
//            check(!(index == 0 || mobs[index - 1] == null))
//            this@ActorList.remove(mobs[index - 1] as T)
//        }
//    }
//
//    fun get(index: Int): T {
//        return mobs[index] as T
//    }
//
//    fun add(mob: T): Boolean {
//        for (id in mobs.indices) {
//            if (mobs[id] == null) {
//                mobs[id] = mob
//                size++
//
//                mob.index = id + 1
//                return true
//            }
//        }
//        return false
//    }
//
//    fun remove(mob: T) {
//        var id = mob.index
//        assert(id != 0)
//        id--
//        assert(mobs[id] === mob)
//        mobs[id] = null
//        size--
//        mob.resetId()
//    }
//    override fun iterator(): MutableIterator<T> {
//        return MobListIterator()
//    }
//}


class ActorList<T>(
    private val initialCapacity: Int,
    private val actors: MutableList<T?> = createMutableList<T?>(initialCapacity)
) : List<T?> by actors {
    override val size: Int get() = actors.count { it != null }
    val indices: IntRange get() = actors.indices
    val capacity: Int get() = actors.size

    @Suppress("UNCHECKED_CAST")
    fun add(actor: Mob): Boolean {
        val index = actors.freeIndex()
        if (index == INVALID_INDEX) return false
        actors[index] = actor as T
        actor.index = index
        return actors[actor.index] != null
    }

    fun remove(actor: Mob): Boolean = when {
        actor.index == INVALID_INDEX -> false
        actors[actor.index] != actor -> false
        else -> {
            actors[actor.index] = null
            actor.resetId()//reset
            actors[actor.index] == null
        }
    }

    override fun isEmpty(): Boolean = size == 0

    private fun <T> List<T>.freeIndex(): Int =
        (INDEX_PADDING until indices.last).firstOrNull { this[it] == null } ?: INVALID_INDEX

    private companion object {
        const val INVALID_INDEX = -1 //0?
        const val INDEX_PADDING = 1

        @Suppress("UNCHECKED_CAST")
        fun <T> createMutableList(size: Int): MutableList<T?> = (arrayOfNulls<Any?>(size) as Array<T?>).toMutableList()
    }
}


class NodeList<T>(
    private val initialCapacity: Int,
    private val entities: MutableList<T?> = createMutableList<T?>(initialCapacity)
) : List<T?> by entities {
    override val size: Int get() = entities.count { it != null }
    val indices: IntRange get() = entities.indices
    val capacity: Int get() = entities.size

    @Suppress("UNCHECKED_CAST")
    fun add(node: Node): Boolean {
        val index = entities.freeIndex()
        if (index == INVALID_INDEX) return false
        entities[index] = node as T
        node.index = index
        return entities[node.index] != null
    }

    fun remove(node: Node): Boolean = when {
        node.index == INVALID_INDEX -> false
        entities[node.index] != node -> false
        else -> {
            entities[node.index] = null
            entities.removeAt(node.index)
            node.index = 0//reset
            entities[node.index] == null
        }
    }

    override fun isEmpty(): Boolean = size == 0

    private fun <T> List<T>.freeIndex(): Int =
        (INDEX_PADDING until indices.last).firstOrNull { this[it] == null } ?: INVALID_INDEX

    private companion object {
        const val INVALID_INDEX = -1 //0?
        const val INDEX_PADDING = 1

        @Suppress("UNCHECKED_CAST")
        fun <T> createMutableList(size: Int): MutableList<T?> = (arrayOfNulls<Any>(size) as Array<T?>).toMutableList()
    }
}

data class ItemTest(val id: Int, var amount: Int) : Node()

fun main() {
    val list = NodeList<ItemTest>(10)
    val item = ItemTest(1, 10)
    list.add(item)

    println("Size: ${list.size}")
    val updated = list.find { it == item }.apply {
        item.amount = 30
    }

    list.filterNotNull().forEach { itemList -> println(itemList) }

}