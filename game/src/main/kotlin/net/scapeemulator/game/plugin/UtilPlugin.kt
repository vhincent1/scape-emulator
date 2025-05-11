package net.scapeemulator.game.plugin

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.command.handleCommand
import net.scapeemulator.game.model.*
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.task.Action
import net.scapeemulator.game.util.displayEnterPrompt
import net.scapeemulator.game.util.removeHintIcon
import net.scapeemulator.game.util.sendHintIcon

val spawnItems = setOf(
    //ancients
    Item(10887, 1),
    Item(13902, 1),
    Item(13899, 1),
    Item(13883, 1),

    //verac
    Item(4753, 1),
    Item(4757, 1),
    Item(4759, 1),

    Item(11694, 1),
    Item(5698, 1),
    Item(882, 1),
    Item(4732, 1),
    Item(4736, 1),
    Item(4738, 1),
    Item(4734, 1)
)

fun spawnBots() {
    val server = GameServer.INSTANCE
    println("Spawning")
    val spawnLocation = Position(3222, 3219)
    val playerBot = Player().apply {
        // session = player.session
        lastKnownRegion = spawnLocation
        username = "test"
        position = spawnLocation
        mostRecentDirection = Direction.NORTH

        //todo fix player equipment
        equipment.add(Item(4732, 1), Equipment.HEAD)
        equipment.add(Item(4736, 1), Equipment.BODY)
        equipment.add(Item(4738, 1), Equipment.LEGS)
        equipment.add(Item(4734, 1), Equipment.WEAPON)
    }
    server.world.players.add(playerBot)
    val action = object : Action<Player>(playerBot, delay = 20, immediate = false) {
        override fun execute() {
            server.world.loginService.addLogoutRequest(mob)
            stop()
        }
    }
//    server.world.taskScheduler.schedule(action)
}

fun utilPlugin() = pluginHandler(
    { event ->
        if (event is LoginEvent) {
            spawnItems.forEach(event.player.inventory::add)
            event.player.sendMessage("Welcome agent <b><img=0>${event.player.username.length} ")

            event.player.send(InteractionOptionMessage(0, "Attack"))
            event.player.send(InteractionOptionMessage(1, "Trade with"))
            event.player.send(InteractionOptionMessage(2, "Request Assit"))
            event.player.send(InteractionOptionMessage(3, "Test"))
            event.player.send(InteractionOptionMessage(4, "Hi"))
            event.player.send(InteractionOptionMessage(6, "6"))
            event.player.send(InteractionOptionMessage(7, "7"))

            val hintIcons = emptyArray<Int>()
//            event.player.send(
//                HintArrowMessage(
//                    slot = 1,
//                    targetType = 10, //1 npc 10 player 2 ground
//
//                    targetId = 1,//idx
//                    entity = event.player,
//                )
//            )
            event.player.sendMessage("PlayerID: ${event.player.id}")

        } else if (event is MessageEvent) {
            val player = event.player
            if (event.message is PlayerInteractionMessage) {
                player.sendMessage("PlayerID: ${event.player.username}")
//                val hint = HintIconMessage(
//                    slot = 1,
//                    target = event.message.target,//idx
//                    entity = event.player,
//                )
//                player.send(hint)
                player.sendHintIcon(0, event.message.target, event.player)
            }
        }

    }, { spawnBots() },
    arrayOf(
        handleCommand("cm") { player, arguments ->
            player.send(ClearMinimapMessage())
//            player.displayEnter(RunScript.Type.STRING) { player, value ->
//                player.sendMessage("VALUE:  $value")
//            }
            player.displayEnterPrompt("Enter String", RunScript.Type.STRING) { player, value ->
                player.sendMessage("Value: $value")
            }
        }, handleCommand("t") { player, arguments ->
            if (arguments.size != 1) {
                player.sendMessage("Syntax ::i [id]")
                return@handleCommand
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
        }, handleCommand("ree") { player, arguments ->
            if (arguments.size != 1) {
                player.sendMessage("Syntax ::r [id]")
                return@handleCommand
            }
            val id = arguments[0].toInt()
            println("removing hint icon $id")
            player.removeHintIcon(id, player)
        }, handleCommand("i") { player, arguments ->
            if (arguments.size != 1) {
                player.sendMessage("Syntax ::i [id]")
                return@handleCommand
            }
            val id = arguments[0].toInt()
            player.interfaceSet.openWindow(id) //667 equip
        }, handleCommand("io") { player, arguments ->
            if (arguments.size != 1) {
                player.sendMessage("Syntax ::io [id]")
                return@handleCommand
            }
            val id = arguments[0].toInt()
            player.interfaceSet.openOverlay(id) //667 equip
        }, handleCommand("iconfig") { player, arguments ->
            if (player.rights < 2) return@handleCommand
            val id = arguments[0].toInt()
            val value = arguments[1].toInt()
            val hidden = arguments.size > 2
            //special bar 301 ?
            player.send(InterfaceVisibleMessage(id, value, hidden))
            player.sendMessage("id: $id, value: $value, hidden: $hidden")
        }, handleCommand("anim") { player, arguments ->
            if (player.rights < 2) return@handleCommand
            if (arguments.isEmpty()) return@handleCommand
            val id = arguments[0].toInt()
            val delay = if (arguments.size != 1) arguments[1].toInt() else 0
            //special bar 301 ?
            println("delay $delay")
            player.playAnimation(Animation(id, delay))
        }
    ), emptyArray()
)