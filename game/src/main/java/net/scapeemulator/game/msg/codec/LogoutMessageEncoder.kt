package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.LogoutMessage
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder

class LogoutMessageEncoder : MessageEncoder<LogoutMessage>(LogoutMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: LogoutMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 86)
        return builder.toGameFrame()
    }
}
