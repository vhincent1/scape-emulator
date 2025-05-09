package net.scapeemulator.game.plugin

import net.scapeemulator.game.command.handleCommand

val loginPlugin = pluginHandler(
    { event ->
        if (event is LoginEvent) {
            event.player.sendMessage("Welcome to the game!")
        }
    }, arrayOf(
        handleCommand("login") { a, b -> },
        handleCommand("login") { a, b -> }
    ), emptyArray())