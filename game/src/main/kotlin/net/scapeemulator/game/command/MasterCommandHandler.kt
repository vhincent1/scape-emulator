package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.SkillSet
import net.scapeemulator.game.msg.codec.InteractionOption

class MasterCommandHandler : CommandHandler("master") {
    override fun handle(player: Player, arguments: Array<String>) {
        player.send(InteractionOption(0, "Attack"))
        if (player.rights < 2) return

        if (arguments.size != 0) {
            player.sendMessage("Syntax: ::master")
            return
        }

        val skills = player.skillSet
        for (id in 0..<skills.size()) skills.addExperience(id, SkillSet.MAXIMUM_EXPERIENCE)
    }
}
