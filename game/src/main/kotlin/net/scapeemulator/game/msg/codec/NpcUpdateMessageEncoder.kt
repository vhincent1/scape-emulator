package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder
import java.io.IOException

class NpcUpdateMessageEncoder : MessageEncoder<NpcUpdateMessage>(NpcUpdateMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: NpcUpdateMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 32, GameFrame.Type.VARIABLE_SHORT)
        val blockBuilder = GameFrameBuilder(alloc)
        builder.switchToBitAccess()

        builder.putBits(8, message.localNpcCount)

        for (descriptor in message.descriptors) descriptor.encode(message, builder, blockBuilder)

        if (blockBuilder.length > 0) {
            builder.putBits(15, 32767)
            builder.switchToByteAccess()
            builder.putRawBuilder(blockBuilder)
        } else {
            builder.switchToByteAccess()
        }

        return builder.toGameFrame()
    }
}
