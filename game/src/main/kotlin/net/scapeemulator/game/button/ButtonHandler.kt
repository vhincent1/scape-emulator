package net.scapeemulator.game.button

import net.scapeemulator.game.model.Player

abstract class ButtonHandler(val id: Int) {
    abstract fun handle(player: Player, slot: Int, parameter: Int)
}

fun handleButton(buttonId: Int, block: (Player, Int, Int) -> Unit): ButtonHandler {
    return object : ButtonHandler(buttonId) {
        override fun handle(player: Player, slot: Int, parameter: Int) {
            block(player, slot, parameter)
        }
    }
}

//fun handleButton(block: (Player, Int, Int) -> Unit): ButtonHandler {
//    return object : ButtonHandler(buttonId) {
//        override fun handle(player: Player, slot: Int, parameter: Int) {
//            block(player, slot, parameter)
//        }
//    }
//}