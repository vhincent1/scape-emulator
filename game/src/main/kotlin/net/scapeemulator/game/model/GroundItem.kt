package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.util.playersWithinScene
import net.scapeemulator.game.util.sendGroundItemCreate
import net.scapeemulator.game.util.sendGroundItemRemoval
import net.scapeemulator.game.util.sendGroundItemUpdate
import java.util.*

//class GroundItem(override var position: Position, val item: Item, val owner: String) : Entity()

data class GroundItem(val id: Int, var amount: Int, val spawn: Position, var owner: Player? = null) : Node() {

    var expire: Int = 0
    var remainPrivate = false

    init {
        super.position = spawn
        resetTimer()
    }

    override var position: Position = spawn
        set(value) { /* ignore */ }

    fun toItem() = Item(id, amount)
    fun resetTimer() {
        expire = 200//if (owner!=null) 100 else 200

        println("Set Timer: $expire")
    }

    override fun toString(): String {
        return "GroundItem(id=$id, amount=$amount, spawn=$spawn, owner=$owner, expire=$expire, " +
                "remainPrivate=$remainPrivate, position=$position)"
    }

}

class GroundItems(nodes: NodeList<GroundItem>) {
    val groundItems: MutableList<GroundItem?> = nodes.toMutableList()

    private fun constructPacket(item: GroundItem, block: Player.() -> Unit) {
        //private
        if (item.owner != null) {
            item.owner?.let {
                if (it.position.isWithinScene(item.position)) block(it)
            }
            return
        }
        //public
        GameServer.WORLD.playersWithinScene(item.position) { block(this) }
    }

    val listeners: List<GroundItemListener> = arrayListOf(object : GroundItemListener() {
        override fun shouldFireEvents(item: GroundItem): Boolean {
            val sameHeight = item.position.height
            return false
        }

        override fun groundItemCreated(item: GroundItem) {
            println("CREATE")
            constructPacket(item) { sendGroundItemCreate(item) }
        }

        override fun groundItemUpdated(item: GroundItem, previousAmount: Int) {
            constructPacket(item) { sendGroundItemUpdate(item, previousAmount) }
        }

        /*
        * Because the remove ground item packet will remove the first item in the clients
        * deque that matches the id and position, it is impossible to remove an item below
        * another with the same id. TODO - Fix this somehow, in the meantime there may be
        * visual inconsistencies if two stackable items with the same ID are dropped and
        * the public item expires first
        */
        override fun groundItemRemoved(item: GroundItem) {
        }

    })

    companion object {}

    fun tick() = groundItems.tick()

    fun refresh() {
//        if (!toRemove.isEmpty()) {
//            for (`object` in toRemove) {
//                val sameHeight = `object`.getPosition().getHeight() == player.getPosition().getHeight()
//                val position: Position = `object`.getPosition()
//                if (sameHeight && player.getPosition().isWithinScene(position)) {
//                    sendPlacementCoords(position)
//                    player.send(
//                        net.scapeemulator.game.msg.impl.grounditem.GroundItemRemoveMessage(
//                            `object`.getItemId(),
//                            position
//                        )
//                    )
//                }
//            }
//            toRemove.clear()
//        }
    }

    fun stack(item: GroundItem): Boolean {
        if (ItemDefinitions.forId(item.id)?.stackable == true) {
            //todo isOwner
//            val previousAmount = item.amount
            val updated = groundItems.find { it == item }?.apply {
                println("Stack idx=$index: $amount to ${amount + item.amount}")
                println("Prev: ${item.amount}")
                amount += item.amount
                println("New amount: $amount")
                val prevTimer = item.expire
                expire = prevTimer
                println("Timer: $expire")
//                resetTimer()
            }

            if (updated != null) {
//                listeners.forEach { it.groundItemUpdated(updated, item.amount) }
                constructPacket(updated) { sendGroundItemUpdate(updated, item.amount) }
                return true
            }
        }
        return false
    }

    fun create(item: GroundItem) {
        //todo item plugins
        val def = ItemDefinitions.forId(item.id)
//        if (def?.tradeable == false) item.private = true
//        println("Private: ${item.private}")

        if (stack(item)) return
        if (!groundItems.add(item)) return

        listeners.forEach { it.groundItemCreated(item) }
        println("DRopped item")
    }

    fun remove(item: GroundItem) {
//        if (!item.removed) return
//        println("rm")

//        GameServer.WORLD.playersWithinScene(item.position) {
//            sendGroundItemRemoval(item.toItem(), item.position)
//        }
//
//        if (!groundItems.remove(item)) return

//        if (item.owner == null) /* public item */ {
//            println("REMOVING ${item.owner}")
//            groundItems.remove(item)
////            if (groundItems.remove(item))
////            getPlayers(item) { sendGroundItemRemoval(item.toItem(), item.position) }
//            GameServer.WORLD.playersWithinScene(item.position) {
//                sendGroundItemRemoval(item.id, item.position)
//            }
//        } else /* change to global item */ {
//            item.private = false
////            item.removed = false
//            item.owner?.apply { sendGroundItemRemoval(item.id, item.position) }
//            item.owner = null
//            item.resetTimer()
//            println("Reset ${item.owner} : ${item.removed} ${item.expire}")
////            if (groundItems.remove(item)) create(item)
//            create(item)
//        }
        listeners.forEach { it.groundItemRemoved(item) }
    }

    val toRemove: MutableList<GroundItem> = LinkedList()

    private fun MutableList<GroundItem?>.tick() {
        val it = this.iterator()
        while (it.hasNext()) {
            val item = it.next() ?: return
            println("Ticking ${item.expire} ${item.toItem()} ${item.position.x} ${item.position.y} ${item.remainPrivate}")
            item.expire--;

            if (item.expire <= 0) {

                if (item.owner == null) /* public item */ {
                    it.remove()

                    constructPacket(item) { sendGroundItemRemoval(item.id, item.position) }
                } else /* private item */ {
                    if (item.remainPrivate) {
                        item.resetTimer()
                        return
                    }

                    item.apply {
                        owner?.apply { sendGroundItemRemoval(item.id, item.position) }
                        owner = null
                        resetTimer()
                        expire = 10
                        constructPacket(this) { sendGroundItemCreate(this@apply) }
                    }
                }
            }
        }
    }
}


abstract class GroundItemListener {
    abstract fun shouldFireEvents(item: GroundItem): Boolean

    abstract fun groundItemCreated(item: GroundItem)

    abstract fun groundItemUpdated(item: GroundItem, previousAmount: Int)

    abstract fun groundItemRemoved(item: GroundItem)
}