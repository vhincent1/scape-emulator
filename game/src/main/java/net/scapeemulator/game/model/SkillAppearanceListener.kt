package net.scapeemulator.game.model

class SkillAppearanceListener(private val player: Player) : SkillListener {
    override fun skillChanged(set: SkillSet, skill: Int) {
        /* empty */
    }

    override fun skillLevelledUp(set: SkillSet, skill: Int) {
//        player.setAppearance(player.getAppearance())
        player.appearance = player.appearance
    }
}
