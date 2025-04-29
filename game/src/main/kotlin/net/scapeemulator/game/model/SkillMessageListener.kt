package net.scapeemulator.game.model

import net.scapeemulator.game.msg.SkillMessage

class SkillMessageListener(private val player: Player) : SkillListener {
    override fun skillChanged(set: SkillSet, skill: Int) {
        val level = set.getCurrentLevel(skill)
        val experience = set.getExperience(skill).toInt()
        player.send(SkillMessage(skill, level, experience))
    }

    override fun skillLevelledUp(set: SkillSet, skill: Int) {
        /* empty */
    }
}
