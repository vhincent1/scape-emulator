package net.scapeemulator.game.plugin.combat

import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.plugin.combat.CombatPlugin.combat
import net.scapeemulator.game.plugin.combat.CombatPlugin.startCombat

const val ARMADYL_GODSWORD = 11694
const val DRAGON_DAGGER = 5698
const val GRANITE_MAUL = 4153
const val VESTAS_LONGSWORD = 13899
const val STATIUS_WARHAMMER = 13902
const val DRAGON_CLAWS = 14484
const val BARRELCHEST_ANCHOR = 10887

fun Combat.autoRetaliate() {
    if (source != null && victim is Player && victim.settings.autoRetaliating)
        if (victim.combat == null) victim.startCombat(source)
}

internal fun getAttackAnimation(weapon: Item?, style: Int): Int {
    val unarmed: Array<Int> = arrayOf(422, 423, 422)
    if (unarmed.getOrNull(style) == null) {
        println("Unknown AttackAnim: item=${weapon?.id} style=$style")
    }

//    if (weapon == null) return unarmed[style]
    val def = weapon?.definition ?: return unarmed[style]
    //check definition
    //check for attack animations
    val attackAnim: Int? = def.attackAnimations?.get(style)
    if (attackAnim != null) return attackAnim
    //otherwise
    var other: Array<Int> = unarmed // unarmed
    var index = style
    if (def.name.contains("warhammer")) other = arrayOf(400, 401, 401)
    if (index > other.size) index = 0
    return other[index]
}