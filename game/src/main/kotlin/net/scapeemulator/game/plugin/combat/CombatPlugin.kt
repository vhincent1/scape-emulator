package net.scapeemulator.game.plugin.combat

import net.scapeemulator.game.command.CommandHandler
import net.scapeemulator.game.model.*
import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.msg.InteractionMessage
import net.scapeemulator.game.msg.WalkMessage
import net.scapeemulator.game.plugin.MessageEvent
import net.scapeemulator.game.plugin.PluginHandler
import net.scapeemulator.game.task.Task
import net.scapeemulator.game.util.sendHintIcon

//todo: when a player logs out
//  target switching/pj'ing

object CombatPlugin {
    const val DEBUG = true
    const val DEFAULT_ATTACK_SPEED = 4

    /* extensions */
    internal var Mob.combat: Combat?
        get() = attributes["combatTask"] as Combat?
        set(value) {
            attributes["combatTask"] = value
        }

    internal var Mob.isAttacking: Boolean
        get() = (attributes["isAttacking"] ?: false) as Boolean
        set(value) {
            attributes["isAttacking"] = value
        }

    internal var Mob.nextAttack: Int
        get() = (attributes["nextAttack"] ?: -1) as Int
        set(value) {
            attributes["nextAttack"] = value
        }

    internal fun Mob.startCombat(target: Mob) = Combat(this, target).apply { combat = this }

    val CombatHandler: (World) -> PluginHandler = { world ->
        PluginHandler(
            init = {
                fun ActorList<Player>.processCombat() {
                    if (isEmpty()) return
                    for (player in this) {
                        if (player == null) continue
                        player.combat?.tick()
                    }
                }

                fun ActorList<Npc>.processCombat() {
                    if (isEmpty()) return
                    for (npc in this) {
                        if (npc == null) continue
                        npc.combat?.tick()
                    }
                }
                /* Combat Tick */
                world.taskScheduler.schedule(Task(1, false) {
                    world.players.processCombat()
                    world.npcs.processCombat()
                })
            },
            handler = { event ->
                if (event !is MessageEvent) return@PluginHandler
                val (player, message) = event
                when (message) {
                    is InteractionMessage -> {
                        val (type, index, option) = message
                        val target = when (type) {
                            InteractionMessage.Type.PLAYER -> world.players[index]
                            InteractionMessage.Type.NPC -> world.npcs[index]
                        } ?: return@PluginHandler
                        player.sendHintIcon(0, index, target)
                        if (option == 0) // attack option
                            player.startCombat(target)
                    }
                    // reset facing
                    is WalkMessage -> if (player.faceTarget != null) player.faceTarget = null
                    /* equipment change */
                    is ButtonMessage -> {//--------------
                        val (id, slot) = message
                        /* all combat interfaces */
                        for (combatInterfaces in Interface.ATTACK_AXE..Interface.ATTACK_WHIP)
                            if (id == combatInterfaces) if (slot == 8 || slot == 10 || slot == 11)
                            /* spec bar */ player.combat?.instantSpec()
                    }//-----------
                }
            },
            commands = arrayOf(CommandHandler("c") { player, strings ->
                player.combat?.stop()
                world.players.onEach { p -> println("${p?.username}=${p?.index}") }
                world.npcs.onEach { npc ->
                    println("NPC=${npc?.index} loc=${npc?.position?.x} ${npc?.position?.y}")
                }
            })
        )
    }
}
