package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.CommandMessage
import net.scapeemulator.game.msg.handler.MessageHandler

class CommandDispatcher : MessageHandler<CommandMessage>() {
    val handlers: MutableMap<String, CommandHandler> = HashMap()

    fun bind(handler: CommandHandler) {
        handlers[handler.name] = handler
    }

    init {
        bind(TeleportCommandHandler())
        bind(ItemCommandHandler())
        bind(ConfigCommandHandler())
        bind(EmptyCommandHandler())
        bind(PositionCommandHandler())
        bind(MasterCommandHandler())
    }

    override fun handle(player: Player, message: CommandMessage) {
        val parts = message.command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val name = parts[0]
        val arguments = arrayOfNulls<String>(parts.size - 1) as Array<String>

        System.arraycopy(parts, 1, arguments, 0, arguments.size)

        val handler = handlers[name]
        handler?.handle(player, arguments) ?: defaultCommandHandler(name, player, arguments)
    }
}