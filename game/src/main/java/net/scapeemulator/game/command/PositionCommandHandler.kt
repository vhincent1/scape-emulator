package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player

class PositionCommandHandler : CommandHandler("pos") {
    override fun handle(player: Player, arguments: Array<String?>) {
        if (player.rights < 2) return

        if (arguments.size != 0) {
            player.sendMessage("Syntax: ::pos")
            return
        }

        val position = player.position
        player.sendMessage("You are at: " + position.x + ", " + position.y + ", " + position.height)
    }
}
