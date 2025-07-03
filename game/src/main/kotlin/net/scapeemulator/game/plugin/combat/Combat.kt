package net.scapeemulator.game.plugin.combat

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.HitType
import net.scapeemulator.game.model.Mob
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.WeaponClass
import net.scapeemulator.game.plugin.combat.CombatPlugin.combat
import net.scapeemulator.game.plugin.combat.CombatPlugin.isAttacking
import net.scapeemulator.game.plugin.combat.CombatPlugin.nextAttack
import net.scapeemulator.game.util.appendHit

class Combat(source: Mob, target: Mob) {
    val source: Mob? = GameServer.WORLD.getMobByIndex(source)
    val victim: Mob? = GameServer.WORLD.getMobByIndex(target)
    private var combatTimeout = 0
    fun getHandler(): CombatHandler? {
        if (source is Player) return when (source.settings.weaponClass) {
            /* magic */
            WeaponClass.STAFF -> {
                // if no spell is selected
                Melee()
            }
            /* ranged weapons */
            WeaponClass.BOW,
            WeaponClass.CHINCHOMPA,
            WeaponClass.CROSSBOW,
            WeaponClass.FIXED_DEVICE,
            WeaponClass.MUD_PIE,
            WeaponClass.SALAMANDER,
            WeaponClass.THROWN -> null
            else -> Melee()
        }
        return null
    }

    internal fun tick() {
        //todo fix when target dies/respawns /isOnline/ Logout
//        val victim = getVictim()
        if (source == null) return
        val handler = getHandler() ?: return

        val nextAttack = source.nextAttack
        if (CombatPlugin.DEBUG) print("next=$nextAttack ")
        if (victim == null) {
            stop()
            return
        }

        if (!handler.canHit(source, victim)) {
            print("timeout=$combatTimeout ")
            if (combatTimeout++ > 10) stop()
            return
        }

        if (source.walkingQueue.isMoving()) stop()

        combatTimeout = 0
        source.isAttacking = true
        source.nextAttack--
        if (nextAttack < 1) {
            source.faceTarget = victim
            val speed = handler.attack(source, victim)
            source.nextAttack = speed
        }

        if (nextAttack <= 0) {
            val damage = handler.impact(source, victim)
            victim.appendHit(damage, if (damage == 0) HitType.NONE else HitType.NORMAL)
            handler.visualImpact(source, victim)
//            target.appendHit(damage, HitType.NONE)
            println("---------------------------------")
            autoRetaliate() // move down
        }
    }

    internal fun stop() {
        if (CombatPlugin.DEBUG) println("STOPPING COMBAT")
        source?.apply {
            nextAttack = -1
            isAttacking = false
            combat = null
            faceTarget = null
        }
    }
}