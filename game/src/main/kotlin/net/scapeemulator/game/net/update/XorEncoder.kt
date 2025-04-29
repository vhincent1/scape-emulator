package net.scapeemulator.game.net.update

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToByteEncoder

class XorEncoder : ByteToByteEncoder() {
    private var key = 0

    fun setKey(key: Int) {
        this.key = key
    }

    public override fun encode(ctx: ChannelHandlerContext?, `in`: ByteBuf, out: ByteBuf) {
        while (`in`.isReadable()) {
            out.writeByte(`in`.readUnsignedByte().toInt() xor key)
        }
    }
}
