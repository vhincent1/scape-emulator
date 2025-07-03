package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.ConfigMessage
import net.scapeemulator.game.msg.InterfaceConfigMessage

class ConfigCommandHandler : CommandHandler("config") {
    override fun handle(player: Player, arguments: Array<String>) {

        player.send(InterfaceConfigMessage(89, 12, true))
        player.send(ConfigMessage(300, 100 * 10))
        player.send(ConfigMessage(301, 1))
        if (player.rights < 2) return

        if (arguments.size != 2) {
            player.sendMessage("Syntax: ::config [id] [value]")
            return
        }

        val id = arguments[0].toInt()
        val value = arguments[1].toInt()
        //special bar 301 ?
        player.send(ConfigMessage(id, value))
    }
}
