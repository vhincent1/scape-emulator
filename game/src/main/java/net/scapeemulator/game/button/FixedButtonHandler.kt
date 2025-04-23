package net.scapeemulator.game.button

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player

class FixedButtonHandler : ButtonHandler(Interface.FIXED) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        if (slot == 66) {
            player.interfaceSet.openWorldMap()
        }
    }
}
