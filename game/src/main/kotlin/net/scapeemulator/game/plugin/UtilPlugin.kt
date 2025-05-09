package net.scapeemulator.game.plugin

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.command.handleCommand
import net.scapeemulator.game.model.*
import net.scapeemulator.game.msg.InterfaceVisibleMessage
import net.scapeemulator.game.task.Action

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

fun spawnBots(server: GameServer) {
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
    server.world.taskScheduler.schedule(action)
}

fun utilPlugin(server: GameServer) = pluginHandler(
    { event ->
        if (event is LoginEvent) {
            spawnItems.forEach(event.player.inventory::add)
            event.player.sendMessage("Welcome agent <b><img=0>${event.player.username.length} ")
        }

    }, { spawnBots(server) },
    arrayOf(
        handleCommand("i") { player, arguments ->
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