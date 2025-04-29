package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.ConfigMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder
import java.io.IOException

class ConfigMessageEncoder : MessageEncoder<ConfigMessage>(ConfigMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: ConfigMessage): GameFrame {
        val id = message.id
        val value = message.value

        if (value >= -128 && value <= 127) {
            val builder = GameFrameBuilder(alloc, 60)
            builder.put(DataType.SHORT, DataTransformation.ADD, id)
            builder.put(DataType.BYTE, DataTransformation.NEGATE, value)
            return builder.toGameFrame()
        } else {
            val builder = GameFrameBuilder(alloc, 226)
            builder.put(DataType.INT, value)
            builder.put(DataType.SHORT, DataTransformation.ADD, id)
            return builder.toGameFrame()
        }
    }
}
