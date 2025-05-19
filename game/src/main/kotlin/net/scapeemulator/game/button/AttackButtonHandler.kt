package net.scapeemulator.game.button

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Tab
import net.scapeemulator.game.model.WeaponClass

class AttackButtonHandler(private val tab: Int, private val styles: IntArray, private val autoRetaliate: Int) :
    ButtonHandler(tab) {

    override fun handle(player: Player, slot: Int, parameter: Int) {
        println("AttackButton Handler $tab $slot $parameter")
        if (player.interfaceSet.getTab(Tab.ATTACK) != tab) return

        val settings = player.settings

        for (style in styles.indices) {
            if (styles[style] == slot) {
                settings.attackStyle = style
            }
        }

        //INDEX = settings.attackStyle <-- attackSlot
        //attackStyles[INDEX] = AttackStyle(CombatSkill, CombatStyle)
        val e = WeaponClass.getAttackStyle(tab, settings.attackStyle)

        println("ATTACK STYLE:  ${e.style}")
        println("SETTINGS STYLE: ${settings.attackStyle}")

        if (slot == autoRetaliate)
            settings.toggleAutoRetaliating()

        if (slot == 8 || slot == 10 || slot == 11)
            settings.setSpecToggle()

    }

    fun configureButton(interfaceId: Int, slot: Int) {
        interfaceId

        //  val style = WeaponClass.forTab(tab, slot)


    }
}
