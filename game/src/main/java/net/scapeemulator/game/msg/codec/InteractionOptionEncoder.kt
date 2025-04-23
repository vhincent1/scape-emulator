package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.net.game.*

class InteractionOption(val index: Int, val name: String) : Message()
class InteractionOptionEncoder : MessageEncoder<InteractionOption>(InteractionOption::class.java) {

    override fun encode(alloc: ByteBufAllocator, message: InteractionOption): GameFrame {
        val builder = GameFrameBuilder(alloc, 44, GameFrame.Type.VARIABLE_BYTE)
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, -1)
        builder.put(DataType.BYTE, if (message.index == 0) 1 else 0)
        builder.put(DataType.BYTE, message.index + 1)
        builder.putString(message.name)
        return builder.toGameFrame()
    }
}