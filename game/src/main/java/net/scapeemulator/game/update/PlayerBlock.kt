package net.scapeemulator.game.update

import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

abstract class PlayerBlock(@JvmField val flag: Int) {
    abstract fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder)
}
