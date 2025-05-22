package net.scapeemulator.game.plugin

import net.scapeemulator.game.command.CommandHandler

val LoginPlugin = PluginHandler(
    { event ->
        if (event is LoginEvent) {
            event.player.sendMessage("Welcome to the game!")
            event.player.sendMessage("World: ${event.player.worldId}")
        }
    }, arrayOf(
        CommandHandler("login") { a, b -> },
        CommandHandler("login") { a, b -> }
    ), emptyArray())
