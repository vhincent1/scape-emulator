package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.Message

abstract class MessageHandler<T : Message> {
    abstract fun handle(player: Player, message: T)
}
