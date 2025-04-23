package net.scapeemulator.game.update

import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

abstract class NpcBlock(val flag: Int) {
    abstract fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder)
}
