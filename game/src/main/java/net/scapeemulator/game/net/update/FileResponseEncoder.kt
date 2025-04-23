package net.scapeemulator.game.net.update

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import java.io.IOException

class FileResponseEncoder : MessageToByteEncoder<FileResponse>() {
    /* TODO: merge with other update encoder? */
    @Throws(IOException::class)
    public override fun encode(ctx: ChannelHandlerContext, response: FileResponse, buf: ByteBuf) {
        val container = response.container
        val type = response.type
        val file = response.file

        buf.writeByte(type)
        buf.writeShort(file)

        var compression = container!!.readUnsignedByte().toInt()
        if (!response.isPriority) compression = compression or 0x80

        buf.writeByte(compression)

        var bytes = container.readableBytes()
        if (bytes > 508) bytes = 508

        buf.writeBytes(container.readBytes(bytes))

        while (true) {
            bytes = container.readableBytes()
            if (bytes == 0) break
            else if (bytes > 511) bytes = 511

            buf.writeByte(0xFF)
            buf.writeBytes(container.readBytes(bytes))
        }
    }
}
