package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException

abstract class MessageDecoder<T : Message>(@JvmField val opcode: Int) {
    @Throws(IOException::class)
    abstract fun decode(frame: GameFrame): T
}

fun <T : Message> MessageDecoder(opcode: Int, block: (frame: GameFrame) -> T): MessageDecoder<T> {
    return object : MessageDecoder<T>(opcode) {
        override fun decode(frame: GameFrame): T {
            return block(frame)
        }
    }
}

//fun <T : Message> handleDecoder(vararg opcode: Int, block: (frame: GameFrame) -> T): MessageDecoder<T> {
//    return object : MessageDecoder<T>(opcode) {
//        override fun decode(frame: GameFrame): T {
//            return block(frame)
//        }
//    }
//}
