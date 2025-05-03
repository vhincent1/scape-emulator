package net.scapeemulator.game.command

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.Equipment
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.InterfaceOpenMessage
import net.scapeemulator.game.task.Action

class CommandDispatcher {
    private val handlers: MutableMap<String, CommandHandler> = HashMap()

    init {
        bind(TeleportCommandHandler())
        bind(ItemCommandHandler())
        bind(ConfigCommandHandler())
        bind(EmptyCommandHandler())
        bind(PositionCommandHandler())
        bind(MasterCommandHandler())
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
