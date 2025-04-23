package net.scapeemulator.game.net.update

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class UpdateStatusMessageEncoder : MessageToByteEncoder<UpdateStatusMessage>() {
    @Throws(Exception::class)
    public override fun encode(ctx: ChannelHandlerContext, msg: UpdateStatusMessage, out: ByteBuf) {
        out.writeByte(msg.status)
    }
}
