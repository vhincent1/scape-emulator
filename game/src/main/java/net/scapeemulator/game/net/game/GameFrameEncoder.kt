package net.scapeemulator.game.net.game

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import net.scapeemulator.util.crypto.StreamCipher

class GameFrameEncoder(private val cipher: StreamCipher) : MessageToByteEncoder<GameFrame>() {
    @Throws(Exception::class)
    public override fun encode(ctx: ChannelHandlerContext, frame: GameFrame, buf: ByteBuf) {
        val type = frame.type
        val payload = frame.payload

        buf.writeByte(frame.opcode + cipher.nextInt())
        if (type == GameFrame.Type.VARIABLE_BYTE) buf.writeByte(payload.readableBytes())
        else if (type == GameFrame.Type.VARIABLE_SHORT) buf.writeShort(payload.readableBytes())

        buf.writeBytes(payload)
    }
}
