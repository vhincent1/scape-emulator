package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.Message

abstract class MessageHandler<out T : Message> {
    abstract fun handle(player: Player, message: @UnsafeVariance T)
}

fun <T : Message> messageHandler(block: (Player, T) -> Unit): MessageHandler<T> {
    return object : MessageHandler<T>() {
        override fun handle(player: Player, message: T) {
            block(player, message)
        }
    }
}