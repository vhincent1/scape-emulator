package net.scapeemulator.game.command

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
    }

    private fun bind(handler: CommandHandler) {
        handlers[handler.name] = handler
    }

    fun handle(player: Player, command: String) {
        val parts = command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val name = parts[0]
        val arguments = arrayOfNulls<String>(parts.size - 1)

        System.arraycopy(parts, 1, arguments, 0, arguments.size)

        val handler = handlers[name]
        handler?.handle(player, arguments)
    }
}
