package net.scapeemulator.game.util

import net.scapeemulator.game.model.Entity
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.RunScript
import net.scapeemulator.game.msg.ClearMinimapMessage
import net.scapeemulator.game.msg.HintIconMessage
import net.scapeemulator.game.msg.InteractionOptionMessage
import net.scapeemulator.game.msg.ScriptMessage

fun Player.sendHintIcon(slot: Int = 0, target: Int, entity: Entity) {
    fun freeSlot(): Int {
        for (i in hintIcons.indices)
            if (hintIcons[i] == null)
                return i
        return -1
    }
    if (freeSlot() == -1)
        for (i in hintIcons.indices)
            hintIcons[i] = null

    println(freeSlot())
    val hint = HintIconMessage(
        slot = freeSlot(),
        target = target,//idx
        entity = entity,
    )
    println("Sending hint icon ${hint.slot}")
    hintIcons[hint.slot] = hint
    send(hint)
}

fun Player.removeHintIcon(slot: Int, entity: Entity) {
    val hint = HintIconMessage(slot = slot, entity = entity)
    hint.remove = true
    hintIcons[hint.slot] = null
    send(hint)
}

fun Player.clearMinimapFlag() {
    send(ClearMinimapMessage())
}

fun Player.sendPlayerOption(slot: Int, option: String) {
    send(InteractionOptionMessage(slot, option))
}

fun Player.displayEnterPrompt(prompt: String, type: RunScript.Type, block: (Player, Any) -> Unit) {
    send(ScriptMessage(type.id, type.types, prompt))
    runScript = RunScript(block)
}