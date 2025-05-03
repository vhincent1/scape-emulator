package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.command.CommandDispatcher
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.CommandMessage

class CommandMessageHandler : MessageHandler<CommandMessage>() {
     val dispatcher = CommandDispatcher()

    override fun handle(player: Player, message: CommandMessage) {
        dispatcher.handle(player, message.command)
    }
}
