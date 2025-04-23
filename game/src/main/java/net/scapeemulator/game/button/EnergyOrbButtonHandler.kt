package net.scapeemulator.game.button

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player

class EnergyOrbButtonHandler : ButtonHandler(Interface.ENERGY_ORB) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        if (slot == 1) {
            player.settings.toggleRunning()
        }
    }
}
