package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceCloseMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder


class InterfaceCloseMessageEncoder : MessageEncoder<InterfaceCloseMessage>(InterfaceCloseMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: InterfaceCloseMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 149)
        builder.put(DataType.SHORT, 0)
//        builder.put(DataType.SHORT, message.id)
//        builder.put(DataType.SHORT, message.slot)
        //TODO check
       // builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
        builder.put(DataType.INT, (message.id shl 16) or message.slot)

        return builder.toGameFrame()
    }
}
