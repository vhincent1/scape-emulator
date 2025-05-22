package net.scapeemulator.game.util

import net.scapeemulator.game.model.*
import net.scapeemulator.game.msg.HintIconMessage
import net.scapeemulator.game.msg.InteractionOptionMessage
import net.scapeemulator.game.msg.ResetMinimapFlagMessage
import net.scapeemulator.game.msg.ScriptMessage

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

fun Player.displayEnterPrompt(prompt: String, type: RunScript.Type, block: (Player, Any) -> Unit) {
    send(ScriptMessage(type.id, type.types, prompt))
    runScript = RunScript(block)
}

fun Mob.appendHit(damage: Int, type: HitType) {
    //todo see how high damage can go
    hitQueue.add(Hit(damage, type))
}

fun Mob.playAnim(id: Int, delay: Int = 0) {
    playAnimation(Animation(id, delay))
}

fun Mob.playGFX(id: Int, delay: Int = 0, height: Int = 0) {
    playSpotAnimation(SpotAnimation(id, delay, height))
}

fun Player.specialBar(amount: Int, add: Boolean) {
    settings.specialEnergy =
        if (add) settings.specialEnergy.plus(amount)
        else
            settings.specialEnergy.minus(amount)
}