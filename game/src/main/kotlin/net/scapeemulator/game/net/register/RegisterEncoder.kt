package net.scapeemulator.game.net.register

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class RegisterEncoder : MessageToByteEncoder<RegisterResponse>() {
    @Throws(Exception::class)
    public override fun encode(ctx: ChannelHandlerContext?, response: RegisterResponse, buf: ByteBuf) {
        buf.writeByte(response.status)
        buf.writeBytes(response.payload)
    }
}
