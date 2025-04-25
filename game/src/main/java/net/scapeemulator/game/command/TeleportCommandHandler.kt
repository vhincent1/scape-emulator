package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Position

class TeleportCommandHandler : CommandHandler("tele") {
    override fun handle(player: Player, arguments: Array<String>) {
        if (player.rights < 2) return

        if (arguments.size != 2 && arguments.size != 3) {
            player.sendMessage("Syntax: ::tele [x] [y] [height=0]")
            return
        }

        val x = arguments[0].toInt()
        val y = arguments[1].toInt()
        var height = player.position.height

        if (arguments.size == 3) height = arguments[2].toInt()

        player.teleport(Position(x, y, height))
    }
}
