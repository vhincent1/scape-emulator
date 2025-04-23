package net.scapeemulator.game.button

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player

class ResizableButtonHandler : ButtonHandler(Interface.RESIZABLE) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        if (slot == 110) {
            player.interfaceSet.openWorldMap()
        }
    }
}
