package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player

class EmptyCommandHandler : CommandHandler("empty") {
    override fun handle(player: Player, arguments: Array<String>) {
        if (player.rights < 2) return

        if (arguments.isNotEmpty()) {
            player.sendMessage("Syntax: ::empty")
            return
        }

        player.inventory.empty()
    }
}
