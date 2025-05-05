package net.scapeemulator.game.plugin

import net.scapeemulator.game.command.CommandHandler

class LoginPlugin : PluginHandler<LoginEvent> {
    override fun handle(entity: LoginEvent) {
        entity.player.sendMessage("Welcome to the game!")
    }

    override fun commands(): Array<CommandHandler> {
        return arrayOf(
            handleCommand("login") { a, b -> }
        )
    }
}