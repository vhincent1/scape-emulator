package net.scapeemulator.game.net.game

import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.msg.codec.CodecRepository
import net.scapeemulator.game.msg.codec.MessageEncoder
import java.io.IOException

class GameMessageEncoder(private val codecs: CodecRepository) : MessageToMessageEncoder<Message>() {

    @Throws(IOException::class)
    @Suppress("UNCHECKED_CAST")
    public override fun encode(ctx: ChannelHandlerContext, message: Message, out: MessageBuf<Any>) {
        val encoder = codecs.get(message.javaClass) as MessageEncoder<Message>?
        encoder?.encode(ctx.alloc(), message)
        out.add(encoder!!.encode(ctx.alloc(), message))
    }
}
