package net.scapeemulator.game.net.world

import io.netty.buffer.ByteBuf
import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class WorldListDecoder : ByteToMessageDecoder() {
    public override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MessageBuf<Any>) {
        if (buf.readableBytes() < 4) return
        val id = buf.readInt()
        println("WorldList Decoder: $id")
        out.add(WorldHandshakeMessage(id))
    }
}
