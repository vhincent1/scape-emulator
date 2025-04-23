package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException

abstract class MessageDecoder<T : Message>(@JvmField val opcode: Int) {
    @Throws(IOException::class)
    abstract fun decode(frame: GameFrame): T
}
