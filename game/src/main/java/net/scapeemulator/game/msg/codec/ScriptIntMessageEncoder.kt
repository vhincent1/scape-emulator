package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.ScriptIntMessage
import net.scapeemulator.game.net.game.*
import java.io.IOException

class ScriptIntMessageEncoder : MessageEncoder<ScriptIntMessage>(ScriptIntMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: ScriptIntMessage): GameFrame {
        val id = message.id
        val value = message.value

        if (value >= -128 && value <= 127) {
            val builder = GameFrameBuilder(alloc, 65)
            builder.put(DataType.SHORT, DataOrder.LITTLE, 0)
            builder.put(DataType.BYTE, DataTransformation.NEGATE, value)
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, id)
            return builder.toGameFrame()
        } else {
            val builder = GameFrameBuilder(alloc, 69)
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, 0)
            builder.put(DataType.INT, value)
            builder.put(DataType.SHORT, DataTransformation.ADD, id)
            return builder.toGameFrame()
        }
    }
}
