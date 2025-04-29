package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceOpenMessage
import net.scapeemulator.game.net.game.*

class InterfaceOpenMessageEncoder : MessageEncoder<InterfaceOpenMessage>(InterfaceOpenMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: InterfaceOpenMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 155)
        builder.put(DataType.BYTE, message.type)
        builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
        builder.put(DataType.SHORT, DataTransformation.ADD, 0)
        builder.put(DataType.SHORT, message.childId)
        return builder.toGameFrame()
    }
}
