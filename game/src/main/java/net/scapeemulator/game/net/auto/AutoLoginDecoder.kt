package net.scapeemulator.game.net.auto

import io.netty.buffer.ByteBuf
import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import net.scapeemulator.util.Base37Utils
import net.scapeemulator.util.ByteBufUtils
import net.scapeemulator.util.crypto.RsaKeySet
import java.io.IOException

class AutoLoginDecoder : ByteToMessageDecoder() {
    enum class State {
        READ_SIZE, READ_PAYLOAD
    }

    private var state = State.READ_SIZE
    private var size = 0

    @Throws(IOException::class)
    public override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MessageBuf<Any>) {
        if (state == State.READ_SIZE) {
            if (!buf.isReadable()) return

            state = State.READ_PAYLOAD
            size = buf.readUnsignedByte().toInt()
        }

        if (state == State.READ_PAYLOAD) {
            if (buf.readableBytes() < size) return

            val encryptedSize = buf.readUnsignedByte().toInt()
            if (encryptedSize != size - 1) throw IOException("Encrypted size mismatch.")

            val secureBuffer = ByteBufUtils.rsa(buf.readBytes(encryptedSize), RsaKeySet.MODULUS, RsaKeySet.PRIVATE_KEY)
            val encryptedType = secureBuffer.readUnsignedByte().toInt()
            if (encryptedType != 10) throw IOException("Invalid encrypted block type.")

            secureBuffer.readUnsignedShort()
            val version = secureBuffer.readUnsignedShort()
            val username = Base37Utils.decodeBase37(secureBuffer.readLong())
            secureBuffer.readInt()
            val password = ByteBufUtils.readString(secureBuffer)
            secureBuffer.readInt()

            out.add(AutoLoginRequest(version, username, password))
        }
    }
}
