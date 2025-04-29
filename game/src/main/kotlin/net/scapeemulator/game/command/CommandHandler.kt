package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player

abstract class CommandHandler(val name: String) {
    abstract fun handle(player: Player, arguments: Array<String>)
}
