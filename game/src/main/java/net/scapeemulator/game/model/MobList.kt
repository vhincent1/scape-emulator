package net.scapeemulator.game.model

class MobList<T : Mob>(capacity: Int) : Iterable<T> {
    private val mobs = arrayOfNulls<Mob>(capacity)
    private var size = 0

    private inner class MobListIterator : MutableIterator<T> {
        private var index = 0

        override fun hasNext(): Boolean {
            for (i in index..<mobs.size) {
                if (mobs[i] != null) return true
            }

            return false
        }

        override fun next(): T {
            while (index < mobs.size) {
                if (mobs[index] != null) return mobs[index++] as T
                index++
            }

            throw NoSuchElementException()
        }

        override fun remove() {
            check(!(index == 0 || mobs[index - 1] == null))

            this@MobList.remove(mobs[index - 1] as T)
        }
    }

    fun add(mob: T): Boolean {
        for (id in mobs.indices) {
            if (mobs[id] == null) {
                mobs[id] = mob
                size++

                mob.id = id + 1
                return true
            }
        }

        return false
    }

    fun remove(mob: T) {
        var id = mob.id
        assert(id != 0)

        id--
        assert(mobs[id] === mob)

        mobs[id] = null
        size--

        mob.resetId()
    }

    override fun iterator(): MutableIterator<T> {
        return MobListIterator()
    }
}
