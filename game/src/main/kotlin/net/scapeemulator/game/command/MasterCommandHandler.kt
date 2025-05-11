package net.scapeemulator.game.command

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.SkillSet

class MasterCommandHandler : CommandHandler("master") {
    override fun handle(player: Player, arguments: Array<String>) {
        if (player.rights < 2) return

        if (arguments.size != 0) {
            player.sendMessage("Syntax: ::master")
            return
        }

        val skills = player.skillSet
        for (id in 0..<skills.size()) skills.addExperience(id, SkillSet.MAXIMUM_EXPERIENCE)
    }
}
