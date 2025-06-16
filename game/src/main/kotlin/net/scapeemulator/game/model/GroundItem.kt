package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.util.playersWithinScene
import net.scapeemulator.game.util.sendGroundItemCreate
import net.scapeemulator.game.util.sendGroundItemRemoval
import net.scapeemulator.game.util.sendGroundItemUpdate

data class GroundItem(val id: Int, var amount: Int, val coords: Position, var owner: Player? = null) : Node() {
    var expire: Int = 0
    var remainPrivate = false
    var worldVisibility = false

    init {
        super.position = coords
        resetTimer()
    }

    override var position: Position = coords
        set(value) { /* ignore */ }

    fun toItem() = Item(id, amount)
    fun resetTimer() {
        expire = 200//if (owner!=null) 100 else 200
        println("Set Timer: $expire")
    }

    override fun toString(): String =
        "GroundItem(id=$id, owner=$owner, amount=$amount, position=$position)"
}

class GroundItemManager(val nodes: NodeList<GroundItem>) {

    fun tick() = nodes.tick()

    private val listeners: List<GroundItemListener> = arrayListOf(
        object : GroundItemListener() {
            //todo: tracker or something
            val log = arrayListOf<String>()
            override fun shouldFireEvents(item: GroundItem): Boolean {
                val sameHeight = item.position.height
                return false
            }

            override fun groundItemCreated(item: GroundItem) {
                val player = item.owner
                if (player != null) log.add(item.toString())
            }

            override fun groundItemUpdated(item: GroundItem, previousAmount: Int) {}
            override fun groundItemRemoved(item: GroundItem) {}
        }
    )

    fun refresh() {}

    fun stack(item: GroundItem): Boolean {
        if (ItemDefinitions.forId(item.id)?.stackable == false) return false
        //todo isOwner
        //val previousAmount = item.amount
        val updated = nodes.find { it == item }?.apply {
            println("Stack idx=$index: $amount to ${amount + item.amount}")
            println("Prev: ${item.amount}")
            amount += item.amount
            println("New amount: $amount")
            val prevTimer = item.expire
            expire = prevTimer
            println("Timer: $expire")
            //resetTimer()
        }
        if (updated != null) {
            constructPacket(updated) { sendGroundItemUpdate(updated, item.amount) }
            listeners.forEach { it.groundItemUpdated(updated, item.amount) }
            return true
        }
        println("Not Found")
        return false
    }

    fun create(item: GroundItem): Boolean {
        //todo item plugins
        if (ItemDefinitions.forId(item.id)?.tradeable == false) item.remainPrivate = true
        if (stack(item)) return true
        if (!nodes.add(item)) return false
        println("Create")
        constructPacket(item) { sendGroundItemCreate(item) }
        listeners.forEach { it.groundItemCreated(item) }
        return true
    }

    fun remove(item: GroundItem): Boolean {
        if (!nodes.remove(item)) return false
        constructPacket(item) { sendGroundItemRemoval(item.id, item.position) }
        listeners.forEach { it.groundItemRemoved(item) }
        return true
    }

    private fun NodeList<GroundItem>.tick() {
        if (isEmpty()) return
        for (item in this) {
            if (item == null) continue
            println("Ticking ${item.expire} ${item.toItem()} ${item.position.x} ${item.position.y} ${item.remainPrivate}")//debug
            if (item.expire-- >= 1) continue

            // respawn
            if (item.worldVisibility) /* public item */ {
                println("Public item")
                this@GroundItemManager.remove(item)
            } else /* private item */ {
                println("Private item")
                if (item.remainPrivate) {
                    item.resetTimer()
                    //todo decide what to do with private items
                    //item.expire = 7
                    //item.remainPrivate = false
                    continue
                }
                // switch to public
                item.apply {
                    owner?.apply { sendGroundItemRemoval(item.id, item.position) }
                    owner = null
                    resetTimer()
                    expire = 10//debug
                    worldVisibility = true
                    constructPacket(this) { sendGroundItemCreate(this@apply) }
                }
            }
        }
    }

    private fun constructPacket(item: GroundItem, block: Player.() -> Unit) = when {
        item.worldVisibility || item.owner == null -> GameServer.WORLD.playersWithinScene(item.position) { block(this) }
        else -> item.owner?.let { if (it.position.isWithinScene(item.position)) block(it) }
    }

//        val dropper = item.owner?.let { if (it.position.isWithinScene(item.position)) block(it) }
//        if (dropper == null) GameServer.WORLD.playersWithinScene(item.position) { block(this) }
}

abstract class GroundItemListener {
    abstract fun shouldFireEvents(item: GroundItem): Boolean
    abstract fun groundItemCreated(item: GroundItem)
    abstract fun groundItemUpdated(item: GroundItem, previousAmount: Int)
    abstract fun groundItemRemoved(item: GroundItem)
}