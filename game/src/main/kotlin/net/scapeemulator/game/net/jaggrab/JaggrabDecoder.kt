package net.scapeemulator.game.net.jaggrab

import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import java.io.IOException

class JaggrabDecoder : MessageToMessageDecoder<String>() {
    @Throws(Exception::class)
    public override fun decode(ctx: ChannelHandlerContext, str: String, out: MessageBuf<Any>) {
        if (!str.startsWith("JAGGRAB ")) throw IOException()

        out.add(JaggrabRequest(str.substring(8)))
    }
}
