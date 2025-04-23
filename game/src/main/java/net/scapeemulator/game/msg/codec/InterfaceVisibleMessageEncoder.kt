package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceVisibleMessage
import net.scapeemulator.game.net.game.*

class InterfaceVisibleMessageEncoder : MessageEncoder<InterfaceVisibleMessage>(InterfaceVisibleMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: InterfaceVisibleMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 21)
        builder.put(DataType.BYTE, DataTransformation.NEGATE, if (message.isVisible) 0 else 1)
        builder.put(DataType.SHORT, 0)
        builder.put(DataType.INT, DataOrder.LITTLE, (message.id shl 16) or message.slot)
        return builder.toGameFrame()
    }
}
