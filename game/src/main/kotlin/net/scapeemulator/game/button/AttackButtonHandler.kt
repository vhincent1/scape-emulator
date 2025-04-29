package net.scapeemulator.game.button

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Tab

class AttackButtonHandler(private val tab: Int, private val styles: IntArray, private val autoRetaliate: Int) :
    ButtonHandler(  tab) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        if (player.interfaceSet.getTab(Tab.ATTACK) != tab) return

        val settings = player.settings

        for (style in styles.indices) {
            if (styles[style] == slot) {
//                settings.setAttackStyle(style)
                settings.attackStyle = style
                return
            }
        }

        if (slot == autoRetaliate) {
            settings.toggleAutoRetaliating()
        }
    }
}
