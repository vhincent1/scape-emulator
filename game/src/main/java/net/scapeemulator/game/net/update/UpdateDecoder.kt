package net.scapeemulator.game.net.update

import io.netty.buffer.ByteBuf
import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class UpdateDecoder : ByteToMessageDecoder() {
    private enum class State {
        READ_VERSION, READ_REQUEST
    }

    private var state = State.READ_VERSION

    @Throws(Exception::class)
    public override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MessageBuf<Any>) {
        if (buf.readableBytes() < 4) return

        if (state == State.READ_VERSION) {
            state = State.READ_REQUEST
            out.add(UpdateVersionMessage(buf.readInt()))
        } else {
            val opcode = buf.readUnsignedByte().toInt()
            if (opcode == 0 || opcode == 1) {
                val type = buf.readUnsignedByte().toInt()
                val file = buf.readUnsignedShort()
                out.add(FileRequest(opcode == 1, type, file))
            } else if (opcode == 4) {
                val key = buf.readUnsignedByte().toInt()
                buf.readerIndex(buf.readerIndex() + 2)
                out.add(UpdateEncryptionMessage(key))
            } else {
                /*
                 * other unused opcodes:
                 *
                 * 2 - logged in
                 * 3 - logged out
                 * 6 - connection initiated
                 * 7 - connection done
                 */
                buf.readerIndex(buf.readerIndex() + 3)
                return  /* TODO print a warning or add support for reading these opcodes? */
            }
        }
    }
}
