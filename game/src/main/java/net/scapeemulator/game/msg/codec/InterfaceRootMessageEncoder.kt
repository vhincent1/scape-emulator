package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceRootMessage
import net.scapeemulator.game.net.game.*

class InterfaceRootMessageEncoder : MessageEncoder<InterfaceRootMessage>(InterfaceRootMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: InterfaceRootMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 145, GameFrame.Type.FIXED)
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.id)
        builder.put(DataType.BYTE, DataTransformation.ADD, message.type)//type
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, 0)//packetcount

        return builder.toGameFrame()
    }
}
