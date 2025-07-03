package net.scapeemulator.game.plugin.combat

import net.scapeemulator.game.model.*
import net.scapeemulator.game.plugin.combat.CombatPlugin.combat
import net.scapeemulator.game.task.Action
import net.scapeemulator.game.util.anim
import net.scapeemulator.game.util.appendHit
import net.scapeemulator.game.util.drainSpecial
import net.scapeemulator.game.util.gfx
import kotlin.math.floor
import kotlin.random.Random

private interface CombatBase {
    fun canHit(attacker: Mob?, victim: Mob?): Boolean
    fun attack(attacker: Mob, victim: Mob): Int
    fun visualImpact(attacker: Mob, victim: Mob)
    fun impact(attacker: Mob, victim: Mob): Int
}

abstract class CombatHandler : CombatBase {
    override fun canHit(attacker: Mob?, victim: Mob?): Boolean {
        if (attacker == null || victim == null) return false
        if (attacker.position.isWithinTileDistance(victim.position, 1)) return true
        return false
    }
}

class Melee : CombatHandler() {

    override fun canHit(attacker: Mob?, victim: Mob?): Boolean {
        return super.canHit(attacker, victim)
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
                    ARMADYL_GODSWORD -> if (attacker.drainSpecial(50)) {
                        attacker.anim(7074)
                        attacker.gfx(1222)
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
                        attacker.combat?.instantSpec()
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
        val hit = calculateHit(attacker, victim)
        return hit
    }

    private fun Player.attackStyle(): AttackStyle.Style =AttackStyle.getStyle(settings.attackStyle)

    fun calculateHit(attacker: Mob, victim: Mob, modifier: Double = 1.0): Int {
        val level = attacker.skillSet.getCurrentLevel(Skill.STRENGTH)
        val bonus = attacker.skillSet.bonuses[11]
        var prayer = 1.0
        if (attacker is Player) {
//            prayer += attacker.prayer.bonus(str)
        }
        var cumulativeStr = floor(level * prayer)
        if (attacker is Player) {
            if (attacker.attackStyle() == AttackStyle.Style.AGGRESSIVE)
                cumulativeStr += 3
            if (attacker.attackStyle() == AttackStyle.Style.CONTROLLED)
                cumulativeStr += 1

            attacker.settings.attackStyle
        }
        cumulativeStr += getSetMultiplier(attacker, Skill.STRENGTH)
        val hit = (16 + cumulativeStr + (bonus / 8) + ((cumulativeStr * bonus) * 0.016865)) * modifier
        return ((hit / 10) + 1).toInt()
    }

    fun getSetMultiplier(attacker: Mob, skill: Int): Double {
        return 1.0
    }
}

