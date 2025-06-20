package net.scapeemulator.game.util

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.*
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.task.Action

fun Player.sendHintIcon(slot: Int?, target: Int, entity: Entity) {
    fun freeSlot(): Int {
        for (i in hintIcons.indices) if (hintIcons[i] == null) return i
        return -1
    }
    if (freeSlot() == -1) for (i in hintIcons.indices) hintIcons[i] = null

//    println("FREESLOT: " + freeSlot())
    val hint = HintIconMessage(
        slot = slot ?: freeSlot(),
        target = target,//idx
        entity = entity,
    )
//    println("Sending hint icon ${hint.slot}")
    hintIcons[hint.slot] = hint
    send(hint)
}

fun Player.removeHintIcon(slot: Int, entity: Entity) {
    val hint = HintIconMessage(slot, entity)
    hint.remove = true
    hintIcons[hint.slot] = null
    send(hint)
}

fun Player.clearMinimapFlag() = send(ResetMinimapFlagMessage())
fun Player.sendPlayerOption(slot: Int, option: String) = send(InteractionOptionMessage(slot, option))
fun Player.displayEnterPrompt(prompt: String, type: RunScriptType, block: (Player, Any) -> Unit) {
    send(ScriptMessage(type.id, type.types, prompt))
    runScript = RunScript(block)
}

fun Mob.appendHit(damage: Int, type: HitType) {
    //todo see how high damage can go
    hitQueue.add(Hit(damage, type))
}

fun Mob.anim(id: Int, delay: Int = 0) = playAnimation(Animation(id, delay))
fun Mob.gfx(id: Int, delay: Int = 0, height: Int = 0) = playSpotAnimation(SpotAnimation(id, delay, height))
fun Player.sound(id: Int, delay: Int = 0, volume: Int = 10) = send(SoundMessage(id, volume, delay))
fun Player.drainSpecial(amount: Int): Boolean {
    if (!settings.specialToggled) return false
    settings.toggleSpecialBar()
    if (amount > settings.specialEnergy) {
        sendMessage("You do not have enough special attack energy left.");
        return false
    }
    settings.specialEnergy -= amount
    return true
}

fun Player.weight(amount: Double) = send(WeightMessage(amount))
fun Player.sendString(id: Int, line: Int, string: String) = send(InterfaceTextMessage(id, line, string))
fun Player.sendAreaUpdate(pos: Position) {
    val chunk = pos.getChunkBase()
    val x: Int = chunk.getRegionX(lastKnownRegion!!)
    val y: Int = chunk.getRegionY(lastKnownRegion!!)
    send(UpdateAreaMessage(x, y))
}

fun Player.sendGroundItemCreate(item: GroundItem) {
    sendAreaUpdate(item.position)
//    send(GroundItemCreateMessage(item.id, item.amount, item.position))
    send(GroundItemMessage(item.id, item.amount, item.position))
}

fun Player.sendGroundItemUpdate(new: GroundItem, previousAmount: Int) {
    println("Updated ${new.amount} : old=${previousAmount}")
    sendAreaUpdate(new.position)
//    send(GroundItemUpdateMessage(new.id, new.amount, new.position, previousAmount))
    send(GroundItemMessage(new.id, new.amount, new.position, previousAmount))
}

fun Player.sendGroundItemRemoval(item: Int, position: Position) {
    sendAreaUpdate(position)
//    send(GroundItemRemoveMessage(item, position))
    send(GroundItemMessage(item, position))
}

/* Ground items */

fun Player.drop(item: Item, preferredSlot: Int = -1): Boolean {
    if (!inventory.contains(item.id)) return false
    val removed = inventory.remove(item, preferredSlot) ?: return false
    val groundItem = GroundItem(removed.id, removed.amount, position, this)
    if (!GameServer.WORLD.items.create(groundItem)) //refund
        inventory.add(item, preferredSlot)
    return true
}

fun Player.pickup(groundItem: GroundItem): Boolean {
    if (ItemDefinitions.forId(groundItem.id)?.groundOptions?.contains("take") == false) return false
    if (!position.isWithinDistance(groundItem.position)) return false
    val droppedBy = groundItem.owner?.let { return@let it.index == index } ?: groundItem.worldVisibility
    if (!droppedBy) {
        sendMessage("Not ur item")
        return false
    }
    val hasRoomFor: (Item) -> Boolean = { it.amount <= inventory.freeSlots(it) }
    if (!hasRoomFor(groundItem.toItem())) return false
    inventory.add(groundItem.toItem())
    startAction(Action(this, 1, false) {
        anim(535)
        sound(2582, volume = 1, delay = 10)
        stop()
    })
    GameServer.WORLD.items.remove(groundItem)
    return true
}

fun Player.totalWealth(): Int {
    var value = 0
    arrayOf(inventory, equipment, bank).onEach { container ->
        val items = container.toArray().filterNotNull()
        items.onEach { item ->
            val alchemyValue = ItemDefinitions.forId(item.id)?.highAlchemy ?: 0
            value += alchemyValue
        }
    }
    return value
}