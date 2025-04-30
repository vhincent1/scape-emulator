package net.scapeemulator.game.command

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.Equipment
import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.Player

class CommandDispatcher {
    private val handlers: MutableMap<String, CommandHandler> = HashMap()

    init {
        bind(TeleportCommandHandler())
        bind(ItemCommandHandler())
        bind(ConfigCommandHandler())
        bind(EmptyCommandHandler())
        bind(PositionCommandHandler())
        bind(MasterCommandHandler())
        bind(object : CommandHandler("p") {
            override fun handle(player: Player, arguments: Array<String>) {

                player.inventory.add(Item(4732, 1), Equipment.HEAD)
                player.inventory.add(Item(4736, 1), Equipment.BODY)
                player.inventory.add(Item(4738, 1), Equipment.LEGS)
                player.inventory.add(Item(4734, 1), Equipment.WEAPON)

                val add = Player().apply {
                    // session = player.session
                    lastKnownRegion = player.position
                    username = "test"
                    position = player.position

                    //todo fix player equipment
                    equipment.add(Item(4732, 1), Equipment.HEAD)
                    equipment.add(Item(4736, 1), Equipment.BODY)
                    equipment.add(Item(4738, 1), Equipment.LEGS)
                    equipment.add(Item(4734, 1), Equipment.WEAPON)

                }
                GameServer.world.players.add(add)
                player.sendMessage("" + GameServer.world.players.size)
            }

        })
    }

    fun bind(handler: CommandHandler) {
        handlers[handler.name] = handler
    }

    fun handle(player: Player, command: String) {
        val parts = command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val name = parts[0]
        val arguments = arrayOfNulls<String>(parts.size - 1) as Array<String>

        System.arraycopy(parts, 1, arguments, 0, arguments.size)

        val handler = handlers[name]
        handler?.handle(player, arguments)
    }
}
