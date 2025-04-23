package net.scapeemulator.game.net.handshake

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundByteHandlerAdapter
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import net.scapeemulator.game.net.auto.AutoLoginDecoder
import net.scapeemulator.game.net.jaggrab.JaggrabDecoder
import net.scapeemulator.game.net.login.LoginDecoder
import net.scapeemulator.game.net.login.LoginEncoder
import net.scapeemulator.game.net.register.RegisterDecoder
import net.scapeemulator.game.net.register.RegisterEncoder
import net.scapeemulator.game.net.update.FileResponseEncoder
import net.scapeemulator.game.net.update.UpdateDecoder
import net.scapeemulator.game.net.update.UpdateStatusMessageEncoder
import net.scapeemulator.game.net.update.XorEncoder
import net.scapeemulator.game.net.world.WorldListDecoder
import net.scapeemulator.game.net.world.WorldListEncoder
import java.io.IOException
import java.nio.charset.StandardCharsets

class HandshakeDecoder : ChannelInboundByteHandlerAdapter() {
    @Throws(IOException::class)
    public override fun inboundBufferUpdated(ctx: ChannelHandlerContext, buf: ByteBuf) {
        if (!buf.isReadable()) return

        val service = buf.readUnsignedByte().toInt()
        var additionalBuf: ByteBuf? = null
        if (buf.isReadable()) {
            additionalBuf = buf.readBytes(buf.readableBytes())
        }

        val pipeline = ctx.pipeline()
        pipeline.remove<HandshakeDecoder?>(HandshakeDecoder::class.java)

        when (service) {
            HandshakeMessage.SERVICE_LOGIN -> pipeline.addFirst(
                LoginEncoder(),
                LoginDecoder()
            )

            HandshakeMessage.SERVICE_UPDATE -> pipeline.addFirst(
                FileResponseEncoder(),
                UpdateStatusMessageEncoder(),
                XorEncoder(),
                UpdateDecoder()
            )

            HandshakeMessage.SERVICE_JAGGRAB -> pipeline.addFirst(
                DelimiterBasedFrameDecoder(1024, *Delimiters.lineDelimiter()),
                StringDecoder(StandardCharsets.ISO_8859_1),
                JaggrabDecoder()
            )

            HandshakeMessage.SERVICE_REGISTER_PERSONAL_DETAILS, HandshakeMessage.SERVICE_REGISTER_USERNAME, HandshakeMessage.SERVICE_REGISTER_COMMIT -> pipeline.addFirst(
                RegisterEncoder(),
                RegisterDecoder(service)
            )

            HandshakeMessage.SERVICE_AUTO_LOGIN -> pipeline.addFirst(
                LoginEncoder(),
                AutoLoginDecoder()
            )

            HandshakeMessage.SERVICE_WORLD_LIST -> pipeline.addFirst(
                WorldListEncoder(),
                WorldListDecoder()
            )

            else -> throw IOException("Invalid service id: " + service + ".")
        }

        ctx.nextInboundMessageBuffer().add(HandshakeMessage(service))
        ctx.fireInboundBufferUpdated()

        if (additionalBuf != null) {
            val head = ctx.pipeline().firstContext()
            head.nextInboundByteBuffer().writeBytes(additionalBuf)
            head.fireInboundBufferUpdated()
        }
    }
}
