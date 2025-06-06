package net.scapeemulator.game.util

import net.scapeemulator.game.model.*
import net.scapeemulator.game.model.GroundItem
import net.scapeemulator.game.msg.*

fun Player.sendHintIcon(slot: Int?, target: Int, entity: Entity) {
    fun freeSlot(): Int {
        for (i in hintIcons.indices)
            if (hintIcons[i] == null)
                return i
        return -1
    }
    if (freeSlot() == -1)
        for (i in hintIcons.indices)
            hintIcons[i] = null

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

fun Player.clearMinimapFlag() {
    send(ResetMinimapFlagMessage())
}

fun Player.sendPlayerOption(slot: Int, option: String) {
    send(InteractionOptionMessage(slot, option))
}

fun Player.displayEnterPrompt(prompt: String, type: RunScriptType, block: (Player, Any) -> Unit) {
    send(ScriptMessage(type.id, type.types, prompt))
    runScript = RunScript(block)
}

fun Mob.appendHit(damage: Int, type: HitType) {
    //todo see how high damage can go
    hitQueue.add(Hit(damage, type))
}

fun Mob.anim(id: Int, delay: Int = 0) {
    playAnimation(Animation(id, delay))
}

fun Mob.gfx(id: Int, delay: Int = 0, height: Int = 0) {
    playSpotAnimation(SpotAnimation(id, delay, height))
}

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

fun Player.weight(amount: Double) {
    send(WeightMessage(amount))
}

fun Player.sendString(id: Int, line: Int, string: String) {
    send(InterfaceTextMessage(id, line, string))
}

fun Player.sendCoords(x: Int, y: Int) {
    val l = lastKnownRegion ?: return
    val localX = position.getLocalX(l.centralRegionX) and 0xfff8
    val localY = position.getLocalY(l.centralRegionY) and 0xfff8
    send(PlacementCoordsMessage(localX, localY))
//        send(GroundItemCreateMessage(Item(995, 1), position))
//    send(GroundItemCreateMessage(removed!!, position))
}

fun Player.sendPlacementCoords(pos: Position) {
    val localX = pos.getLocalX(position.centralRegionX) and 0xfff8
    val localY = pos.getLocalY(position.centralRegionY) and 0xfff8
    send(PlacementCoordsMessage(localX, localY))
}

fun Player.sendCoords3(pos: Position) {
    val x: Int = pos.getSceneX(lastKnownRegion!!)
    val y: Int = pos.getSceneY(lastKnownRegion!!)
    send(PlacementCoordsMessage(x, y))
}

fun Player.sendCoords2(l: Position) {
//    val l = lastKnownRegion ?: return
    val localX = position.getLocalX(l.centralRegionX) and 0xfff8
    val localY = position.getLocalY(l.centralRegionY) and 0xfff8
    send(PlacementCoordsMessage(localX, localY))
//        send(GroundItemCreateMessage(Item(995, 1), position))
//    send(GroundItemCreateMessage(removed!!, position))
}

//fun sendCoords(location: com.xeno.model.Location) {
//    val spb: StaticPacketBuilder = StaticPacketBuilder().setId(26)
//    val regionX: Int = player.getUpdateFlags().getLastRegion().getRegionX()
//    val regionY: Int = player.getUpdateFlags().getLastRegion().getRegionY()
//    spb.addByteC((location.getX() - ((regionX - 6) * 8)).toByte().toInt())
//    spb.addByte((location.getY() - ((regionY - 6) * 8)).toByte())
//    player.getSession().write(spb.toPacket())
//}

fun Player.sendGroundItem(item: GroundItem) {
//    sendCoords(position.x, position.y)
    sendCoords3(item.position.getChunkBase())
    send(GroundItemCreateMessage(item.toItem(), item.position))
}

fun Player.sendGroundItemUpdate(new: Item, old: GroundItem) {
    println("Updated ${new.amount} : old=${old.amount}")
    sendCoords(new.position.x, new.position.y)
//    send(GroundItemUpdateMessage(new, old.item, position))
    send(GroundItemUpdateMessage(new.id, new.amount, old.position, old.previousAmount))
}

fun Player.sendGroundItemRemoval(item: Item, position: Position) {
    send(GroundItemRemoveMessage(item, position))
}