package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.ResetMinimapFlagMessage
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder
import java.io.IOException

class ResetMinimapFlagMessageEncoder : MessageEncoder<ResetMinimapFlagMessage>(ResetMinimapFlagMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: ResetMinimapFlagMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 153)
        return builder.toGameFrame()
    }
}
