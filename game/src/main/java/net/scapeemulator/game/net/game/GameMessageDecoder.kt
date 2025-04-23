package net.scapeemulator.game.net.game

import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import net.scapeemulator.game.msg.codec.CodecRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

class GameMessageDecoder(private val codecs: CodecRepository) : MessageToMessageDecoder<GameFrame>() {

    @Throws(IOException::class)
    public override fun decode(ctx: ChannelHandlerContext, frame: GameFrame, out: MessageBuf<Any>) {
        val decoder = codecs.get(frame.opcode)

        //TODO check
        if (decoder == null) {
            logger.warn("No decoder for packet id " + frame.opcode + ".")
            return
        }

        out.add(decoder.decode(frame))
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GameMessageDecoder::class.java)
    }
}
