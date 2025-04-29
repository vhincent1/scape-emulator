package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.ConfigMessage

class ConfigCommandHandler : CommandHandler("config") {
    override fun handle(player: Player, arguments: Array<String>) {
        if (player.rights < 2) return

        if (arguments.size != 2) {
            player.sendMessage("Syntax: ::config [id] [value]")
            return
        }

        val id = arguments[0]!!.toInt()
        val value = arguments[1]!!.toInt()

        player.send(ConfigMessage(id, value))
    }
}
