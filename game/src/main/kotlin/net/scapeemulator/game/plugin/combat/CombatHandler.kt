package net.scapeemulator.game.plugin.combat

import net.scapeemulator.game.model.*
import net.scapeemulator.game.plugin.combat.CombatPlugin.combat
import net.scapeemulator.game.task.Action
import net.scapeemulator.game.util.anim
import net.scapeemulator.game.util.appendHit
import net.scapeemulator.game.util.drainSpecial
import net.scapeemulator.game.util.gfx
import kotlin.random.Random

abstract class CombatHandler {

    abstract fun canHit(attacker: Mob?, victim: Mob?): Boolean
    abstract fun attack(attacker: Mob, victim: Mob): Int
    abstract fun visualImpact(attacker: Mob, victim: Mob)
    abstract fun impact(attacker: Mob, victim: Mob): Int
}

class Melee : CombatHandler() {
    override fun canHit(attacker: Mob?, victim: Mob?): Boolean {
        if (attacker == null || victim == null) return false
        if (attacker.position.isWithinDistance(victim.position, 1)) return true
        return false
    }

    override fun attack(attacker: Mob, victim: Mob): Int {
        if (attacker is Player) {
            val weapon = attacker.equipment.get(Equipment.WEAPON)

            // play attack animation
            val attackAnim = getAttackAnimation(weapon, attacker.settings.attackStyle)
            attacker.playAnimation(Animation(attackAnim))

            //todo special energy check
            if (attacker.settings.specialToggled) {
                when (weapon?.id) {
                    ARMADYL_GODSWORD -> {
                        if (attacker.drainSpecial(50)) {
                            attacker.anim(7074)
                            attacker.gfx(1222)
                        }
                    }

                    DRAGON_DAGGER -> {
                        val hit1 = Hit(Random.nextInt(20), HitType.DISEASE)
                        val hit2 = Hit(Random.nextInt(49), HitType.NORMAL)
                        attacker.anim(1062)
                        attacker.gfx(252, height = 96)
                        victim.appendHit(1, HitType.NORMAL)
                        victim.appendHit(2, HitType.POISON)
                    }

                    GRANITE_MAUL -> {
                        attacker.combat()?.instantSpec()
                    }

                    VESTAS_LONGSWORD -> {
                        attacker.anim(10502)
                    }

                    STATIUS_WARHAMMER -> {
                        attacker.anim(10505)
                    }

                    DRAGON_CLAWS -> {
                        attacker.anim(10961)
                        victim.appendHit(1, HitType.NORMAL)
                        victim.appendHit(2, HitType.NORMAL)
                        val calcHit = Action(attacker, 1, false) {
                            victim.appendHit(3, HitType.NORMAL)
                            victim.appendHit(4, HitType.NORMAL)
                            stop()
                        }
                        victim.startAction(calcHit)
                    }

                    BARRELCHEST_ANCHOR -> {
                        attacker.anim(5870)
                    }
                }
                // toggle spec
//                attacker.settings.toggleSpecialBar()
            }

            // tick speed
            val speed = weapon?.definition?.attackSpeed ?: CombatPlugin.DEFAULT_ATTACK_SPEED
            return speed
        }
        return CombatPlugin.DEFAULT_ATTACK_SPEED
    }

    override fun visualImpact(attacker: Mob, victim: Mob) {
        victim.anim(424)
    }

    override fun impact(attacker: Mob, victim: Mob): Int {
        victim.appendHit(1, HitType.NONE)
        return 1
    }
}

