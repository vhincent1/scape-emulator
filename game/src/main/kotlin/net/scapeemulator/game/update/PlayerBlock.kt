package net.scapeemulator.game.update

import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

abstract class PlayerBlock(@JvmField val flag: Int) {
    abstract fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder)
}

internal fun playerBlock(flag: Int, block: (PlayerUpdateMessage, GameFrameBuilder) -> Unit): PlayerBlock {
    return object : PlayerBlock(flag) {
        override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
            block(message, builder)
        }
    }
}
