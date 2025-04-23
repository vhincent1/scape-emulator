package net.scapeemulator.game.button

import net.scapeemulator.game.model.Player

abstract class ButtonHandler(val id: Int) {
    abstract fun handle(player: Player, slot: Int, parameter: Int)
}
