package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceTextMessage
import net.scapeemulator.game.net.game.*

class InterfaceTextMessageEncoder : MessageEncoder<InterfaceTextMessage>(InterfaceTextMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: InterfaceTextMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 171, GameFrame.Type.VARIABLE_SHORT)
        builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
        builder.putString(message.text)
        builder.put(DataType.SHORT, DataTransformation.ADD, 0)
        return builder.toGameFrame()
    }
}
