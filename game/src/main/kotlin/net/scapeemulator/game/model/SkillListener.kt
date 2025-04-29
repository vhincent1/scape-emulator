package net.scapeemulator.game.model

interface SkillListener {
    fun skillChanged(set: SkillSet, skill: Int)

    fun skillLevelledUp(set: SkillSet, skill: Int)
}
