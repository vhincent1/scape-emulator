package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.util.playersWithinScene
import net.scapeemulator.game.util.sendGroundItemCreate
import net.scapeemulator.game.util.sendGroundItemRemoval
import net.scapeemulator.game.util.sendGroundItemUpdate

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

class GroundItemManager(val nodes: NodeList<GroundItem>) {

    fun tick() = nodes.tick()

    private val listeners: List<GroundItemListener> = arrayListOf(object : GroundItemListener() {
        override fun shouldFireEvents(item: GroundItem): Boolean {
            val sameHeight = item.position.height
            return false
        }

        override fun groundItemCreated(item: GroundItem) {}
        override fun groundItemUpdated(item: GroundItem, previousAmount: Int) {}
        override fun groundItemRemoved(item: GroundItem) {}
    })

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
            val updated = nodes.find { it == item }?.apply {
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

    fun create(item: GroundItem, created: (Boolean) -> Unit) {
        //todo item plugins
        if (ItemDefinitions.forId(item.id)?.tradeable == false) item.remainPrivate = true
        if (stack(item)) return created(true)
        if (!nodes.add(item)) return created(false)
        constructPacket(item) { sendGroundItemCreate(item) }
//        listeners.forEach { it.groundItemCreated(item) }
        println("Dropped item")
        return created(true)
    }

    fun remove(item: GroundItem, result: (Boolean) -> Unit) {
        if (!nodes.remove(item)) return result(false)
        constructPacket(item) { sendGroundItemRemoval(item.id, item.position) }
//        listeners.forEach { it.groundItemRemoved(item) }
        return result(true)
    }

    private fun NodeList<GroundItem>.tick() {
        if (isEmpty()) return
        for (item in this) {
            if (item == null) continue
            println("Ticking ${item.expire} ${item.toItem()} ${item.position.x} ${item.position.y} ${item.remainPrivate}")
            if (item.expire-- >= 1) continue

            if (item.owner == null) /* public item */ {
                remove(item)
                constructPacket(item) { sendGroundItemRemoval(item.id, item.position) }
            } else /* private item */ {
                if (item.remainPrivate) {
                    item.resetTimer()
                    //todo decide what to do with private items
                    //item.expire = 7
                    //item.remainPrivate = false
                    continue
                }

                item.apply {
                    owner?.apply { sendGroundItemRemoval(item.id, item.position) }
                    owner = null
                    resetTimer()
                    expire = 10//debug
                    constructPacket(this) { sendGroundItemCreate(this@apply) }
                }
                //listeners.forEach { it.groundItemRemoved(item) }
            }
        }
    }

    private fun constructPacket(item: GroundItem, block: Player.() -> Unit) {
        val dropper = item.owner?.let { if (it.position.isWithinScene(item.position)) block(it) }
        if (dropper == null) GameServer.WORLD.playersWithinScene(item.position) { block(this) }
    }
}

abstract class GroundItemListener {
    abstract fun shouldFireEvents(item: GroundItem): Boolean
    abstract fun groundItemCreated(item: GroundItem)
    abstract fun groundItemUpdated(item: GroundItem, previousAmount: Int)
    abstract fun groundItemRemoved(item: GroundItem)
}
