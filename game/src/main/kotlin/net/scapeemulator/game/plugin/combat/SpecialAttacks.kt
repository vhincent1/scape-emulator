package net.scapeemulator.game.plugin.combat

import net.scapeemulator.game.model.Equipment
import net.scapeemulator.game.model.HitType
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.plugin.combat.CombatPlugin.combat
import net.scapeemulator.game.util.anim
import net.scapeemulator.game.util.appendHit
import net.scapeemulator.game.util.gfx

fun Combat.instantSpec() {
    if (source !is Player) return
    if (!source.combat?.getHandler()?.canHit(source, victim)!!) return
    val weapon = source.equipment.get(Equipment.WEAPON) ?: return
    if (!source.settings.specialToggled) return
    when (weapon.id) {
        4153 -> {//gmaul
            //todo check distance
            // canAttack
            // inflict damage
            source.anim(1667)
            source.gfx(340)
//            target.appendHit(100, HitType.NORMAL)
            victim?.appendHit(100, HitType.NORMAL)
//                mob.specialBar(50, false)
            source.settings.toggleSpecialBar()
            //inflictDamage
        }
    }
//        mob.settings.toggleSpecialBar()
}
