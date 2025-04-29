package net.scapeemulator.game.net.login

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class LoginEncoder : MessageToByteEncoder<LoginResponse>() {
    public override fun encode(ctx: ChannelHandlerContext, response: LoginResponse, buf: ByteBuf) {
        buf.writeByte(response.status)
        buf.writeBytes(response.payload)

        if (response.status != LoginResponse.STATUS_EXCHANGE_KEYS) ctx.pipeline().remove(this)
    }
}
