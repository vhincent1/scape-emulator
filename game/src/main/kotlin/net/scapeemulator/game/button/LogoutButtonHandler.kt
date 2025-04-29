package net.scapeemulator.game.button

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player

class LogoutButtonHandler : ButtonHandler(Interface.LOGOUT) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        if (slot == 6) {
            player.logout()
        }
    }
}
