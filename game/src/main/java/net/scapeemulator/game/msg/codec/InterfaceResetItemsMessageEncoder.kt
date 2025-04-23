package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceResetItemsMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder
import java.io.IOException

class InterfaceResetItemsMessageEncoder :
    MessageEncoder<InterfaceResetItemsMessage>(InterfaceResetItemsMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: InterfaceResetItemsMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 144)
        builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
        return builder.toGameFrame()
    }
}
