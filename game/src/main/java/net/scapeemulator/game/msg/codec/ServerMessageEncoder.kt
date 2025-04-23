package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.ServerMessage
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder

class ServerMessageEncoder : MessageEncoder<ServerMessage>(ServerMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: ServerMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 70, GameFrame.Type.VARIABLE_BYTE)
        builder.putString(message.text)
        return builder.toGameFrame()
    }
}
