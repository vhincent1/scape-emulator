package net.scapeemulator.game.button

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Tab

class AttackButtonHandler(private val tab: Int, private val styles: IntArray, private val autoRetaliate: Int) :
    ButtonHandler(tab) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        println("AttackButton Handler $tab $slot $parameter")
        if (player.interfaceSet.getTab(Tab.ATTACK) != tab) return

        val settings = player.settings

        for (style in styles.indices) {
            if (styles[style] == slot) {
//                settings.setAttackStyle(style)
                settings.attackStyle = style
                println("AttackStyle $style") //todo get style name
                return
            }
        }

        if (slot == autoRetaliate) {
            settings.toggleAutoRetaliating()
        }

        //todo special bar
        settings.setSpec(100)
        if (slot == 8 || slot == 10) settings.setSpecToggle()
    }
}
