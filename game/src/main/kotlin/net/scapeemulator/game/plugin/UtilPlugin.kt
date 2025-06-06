package net.scapeemulator.game.plugin

import net.scapeemulator.game.command.CommandHandler
import net.scapeemulator.game.model.*
import net.scapeemulator.game.msg.HintIconMessage
import net.scapeemulator.game.msg.InterfaceVisibleMessage
import net.scapeemulator.game.task.Action
import net.scapeemulator.game.util.displayEnterPrompt
import net.scapeemulator.game.util.removeHintIcon
import net.scapeemulator.game.util.sendPlayerOption

val spawnItems = setOf(
    //ancients
    Item(4675),
    Item(10887),
    Item(13902),
    Item(13899), //vls
    Item(13883),
    Item(14484), //dclaws
    //verac
    Item(4753),
    Item(4757),
    Item(4759),
    Item(11694),
    Item(5698),
    Item(882),
    Item(4732),
    Item(4736),
    Item(4738),
    Item(4734),
    Item(995, 69),
    Item(4153)
)

fun Int.m(): Int = this * 1_000_000

private lateinit var playerBot: Player
fun spawnBots(world: World) {
//    val world = GameServer.INSTANCE.world
    println("Spawning")
    val spawnLocation = Position(3222, 3219)

    val npc = Npc(1).apply { position = Position(3221, 3219) }
    world.npcs.add(npc)

    playerBot = Player().apply {
        lastKnownRegion = spawnLocation
        username = "test"
        position = spawnLocation
        mostRecentDirection = Direction.NORTH

        settings.autoRetaliating = true

        appearance = generateAppearance(Gender.MALE)
        equipment.add(Item(4732), Equipment.HEAD)
        equipment.add(Item(4736), Equipment.BODY)
        equipment.add(Item(4738), Equipment.LEGS)
        equipment.add(Item(4734), Equipment.WEAPON)
    }
    world.players.add(playerBot)
    val action = object : Action<Player>(playerBot, delay = 20, immediate = false) {
        override fun execute() {
//            server.world.loginService.addLogoutRequest(mob)
            stop()
        }
    }
//    world.taskScheduler.schedule(action)
}

val UtilPlugin: (World) -> PluginHandler = { world ->
    PluginHandler(
        { event ->
            if (event is LoginEvent) {
                spawnItems.onEach(event.player.inventory::add)
//            event.player.sendMessage("Welcome agent <b><img=0>${event.player.username.length} ")
                event.player.sendPlayerOption(0, "Attack")
                event.player.sendPlayerOption(1, "Trade with")
                event.player.sendPlayerOption(2, "Request Assist")
                event.player.sendPlayerOption(3, "Test")
                event.player.sendPlayerOption(4, "Hi")
                event.player.sendPlayerOption(6, "6")
                event.player.sendPlayerOption(7, "7")
            }

        }, { spawnBots(world) },
        arrayOf(
            CommandHandler("bandos") { player, args ->
                player.equipment.add(Item(11335), Equipment.HEAD)
                player.equipment.add(Item(6570), Equipment.CAPE)
                player.equipment.add(Item(11724), Equipment.BODY)
                player.equipment.add(Item(11726), Equipment.LEGS)
                player.equipment.add(Item(11732), Equipment.FEET)
                player.equipment.add(Item(7462), Equipment.HANDS)
            },
            CommandHandler("npc") { player, arguments ->
                if (arguments.size != 1) {
                    player.sendMessage("Syntax ::npc [id]")
                    return@CommandHandler
                }
                val id = arguments[0].toInt()
                val npc = Npc(id).apply {
                    position = player.position.apply {
                        x + 1
                    }
                }
                //world.npcs.add(npc)
            }, CommandHandler("rm") { player, arguments ->
                if (!world.players.remove(playerBot))
                    world.players.add(playerBot)
            },
            CommandHandler("search") { player, arguments ->
                player.displayEnterPrompt("pickup Item", RunScriptType.STRING) { player, value ->
                    player.sendMessage("Search: $value")

//                val search = value as String
//
//                val found = ItemDefinitions.getDefinitions().filter {
//                    it.value.name.contains(search)
//                    !it.value.unnoted
//                }
//                player.sendMessage("Found: ${found.size}")

                }
            },
            CommandHandler("cm") { player, arguments ->
//            player.displayEnter(RunScript.Type.STRING) { player, value ->
//                player.sendMessage("VALUE:  $value")
//            }
                player.displayEnterPrompt("Enter String", RunScriptType.STRING) { player, value ->
                    player.sendMessage("Value: $value")
                }
            }, CommandHandler("t") { player, arguments ->

                if (arguments.size != 1) {
                    player.sendMessage("Syntax ::i [id]")
                    return@CommandHandler
                }
                val id = arguments[0].toInt() ?: 2
//            val arrowId = arguments[1].toInt() ?: 1
                player.sendMessage("TargetType: $id")
                val hint = HintIconMessage(
                    slot = -1,
//                targetType = 2, //1 npc 10 player 2 ground
//                targetId = 1,//idx
                    position = player.position
                )
                player.send(hint)
            }, CommandHandler("ree") { player, arguments ->
                if (arguments.size != 1) {
                    player.sendMessage("Syntax ::r [id]")
                    return@CommandHandler
                }
                val id = arguments[0].toInt()
                println("removing hint icon $id")
                player.removeHintIcon(id, player)
            }, CommandHandler("i") { player, arguments ->
                if (arguments.size != 1) {
                    player.sendMessage("Syntax ::i [id]")
                    return@CommandHandler
                }
                val id = arguments[0].toInt()
                player.interfaceSet.openWindow(id) //667 equip
            }, CommandHandler("io") { player, arguments ->
                if (arguments.size != 1) {
                    player.sendMessage("Syntax ::io [id]")
                    return@CommandHandler
                }
                val id = arguments[0].toInt()
                player.interfaceSet.openOverlay(id) //667 equip
            }, CommandHandler("iconfig") { player, arguments ->
                if (player.rights < 2) return@CommandHandler
                val id = arguments[0].toInt()
                val value = arguments[1].toInt()
                val hidden = arguments.size > 2
                //special bar 301 ?
                player.send(InterfaceVisibleMessage(id, value, hidden))
                player.sendMessage("id: $id, value: $value, hidden: $hidden")
            }, CommandHandler("anim") { player, arguments ->
                if (player.rights < 2) return@CommandHandler
                if (arguments.isEmpty()) return@CommandHandler
                val id = arguments[0].toInt()
                val delay = if (arguments.size != 1) arguments[1].toInt() else 0
                //special bar 301 ?
                println("delay $delay")
                player.playAnimation(Animation(id, delay))
            }
        ), emptyArray()
    )
}