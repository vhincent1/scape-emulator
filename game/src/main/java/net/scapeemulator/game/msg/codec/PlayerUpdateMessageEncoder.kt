package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder
import java.io.IOException

class PlayerUpdateMessageEncoder : MessageEncoder<PlayerUpdateMessage>(PlayerUpdateMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: PlayerUpdateMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 225, GameFrame.Type.VARIABLE_SHORT)
        val blockBuilder = GameFrameBuilder(alloc)
        builder.switchToBitAccess()

        message.selfDescriptor.encode(message, builder, blockBuilder)
        builder.putBits(8, message.localPlayerCount)

        for (descriptor in message.descriptors) descriptor.encode(message, builder, blockBuilder)

        if (blockBuilder.length > 0) {
            builder.putBits(11, 2047)
            builder.switchToByteAccess()
            builder.putRawBuilder(blockBuilder)
        } else {
            builder.switchToByteAccess()
        }

        return builder.toGameFrame()
    }
}
