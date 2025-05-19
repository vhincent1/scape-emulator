package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player

abstract class CommandHandler(val name: String) {
    abstract fun handle(player: Player, arguments: Array<String>)
}

internal fun CommandHandler(name: String, block: (Player, Array<String>) -> Unit): CommandHandler {
    return object : CommandHandler(name) {
        override fun handle(player: Player, arguments: Array<String>) {
            block(player, arguments)
        }
    }
}
