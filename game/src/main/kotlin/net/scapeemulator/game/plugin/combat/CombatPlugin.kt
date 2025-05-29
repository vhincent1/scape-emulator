package net.scapeemulator.game.plugin.combat

import net.scapeemulator.game.command.CommandHandler
import net.scapeemulator.game.model.*
import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.msg.InteractionMessage
import net.scapeemulator.game.msg.InteractionType
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

    /* plugin variables/declarations */
    private val combat: (Mob) -> Combat? = { it.attributes["combatTask"] as Combat? }
    private val setCombatTask: (Mob, Combat?) -> Unit = { p, t -> p.attributes["combatTask"] = t }

    // ? move to CombatTask class
    internal val isAttacking: (Mob) -> Boolean = { (it.attributes["isAttacking"] ?: false) as Boolean }
    internal val setIsAttacking: (Mob, Boolean) -> Unit = { p, v -> p.attributes["isAttacking"] = v }
    internal val nextAttack: (Mob) -> Int = { (it.attributes["nextAttack"] ?: -1) as Int }
    internal val setNextAttack: (Mob, Int) -> Unit = { p, v -> p.attributes["nextAttack"] = v }

    /* extensions */
    internal fun Mob.combat() = combat(this)
    internal fun Mob.setCombat(combat: Combat?) = setCombatTask(this, combat)

    val CombatHandler: (World) -> PluginHandler = { world ->
        PluginHandler(
            init = {
                fun ActorList<Player>.processCombat() {
                    if (isEmpty()) return
                    for (player in this) {
                        if (player == null) continue
                        player.combat()?.tick()
                    }
                }
                /* Combat Tick */
                world.taskScheduler.schedule(Task(1, false) {
                    world.players.processCombat()
                })
            },
            handler = { event ->
                if (event !is MessageEvent) return@PluginHandler
                val (player, message) = event
                when (message) {
                    is InteractionMessage -> {
                        val (type, index, option) = message
                        val target = if (type == InteractionType.NPC)
                            world.npcs[index] else world.players[index]

                        if (target == null) return@PluginHandler

                        player.sendHintIcon(0, index, target)
                        if (option == 0) // attack option
                            player.setCombat(Combat(player, target))
                    }

                    // reset facing
                    is WalkMessage -> if (player.faceMob != null) player.faceMob = null

                    /* equipment change */
                    is ButtonMessage -> {//--------------
                        val (id, slot) = message

                        /* all combat interfaces */
                        for (combatInterfaces in Interface.ATTACK_AXE..Interface.ATTACK_WHIP) {
                            if (id == combatInterfaces) {
                                /* spec bar */
                                if (slot == 8 || slot == 10 || slot == 11)
//                                    combat(player)?.instantSpec()
                                    player.combat()?.instantSpec()
                            }
                        }
                    }//-----------
                }

            },
            commands = arrayOf(CommandHandler("c") { player, strings ->
                combat(player)?.stop()
                world.players.onEach { p ->
                    println("${p?.username}=${p?.index}")
                }
                world.npcs.onEach { npc ->
                    println("NPC=${npc?.index} loc=${npc?.position?.x} ${npc?.position?.y}")
                }
            })
        )
    }
}
