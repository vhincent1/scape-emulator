package net.scapeemulator.game.net.register

import io.netty.buffer.ByteBuf
import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import net.scapeemulator.game.net.handshake.HandshakeMessage
import net.scapeemulator.util.Base37Utils
import net.scapeemulator.util.ByteBufUtils
import net.scapeemulator.util.crypto.RsaKeySet
import java.io.IOException
import java.util.*

class RegisterDecoder(private val service: Int) : ByteToMessageDecoder() {
    enum class State {
        READ_SIZE,
        READ_PAYLOAD
    }

    private var size = 0
    private var state = State.READ_SIZE

    @Throws(Exception::class)
    public override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MessageBuf<Any>) {
        if (service == HandshakeMessage.SERVICE_REGISTER_PERSONAL_DETAILS) {
            if (buf.readableBytes() < 6) return

            val day = buf.readUnsignedByte().toInt()
            val month = buf.readUnsignedByte().toInt()
            val year = buf.readUnsignedShort()
            val country = buf.readUnsignedShort()
            out.add(RegisterPersonalDetailsRequest(GregorianCalendar(year, month, day), country))
        } else if (service == HandshakeMessage.SERVICE_REGISTER_USERNAME) {
            if (buf.readableBytes() < 8) return

            val username = Base37Utils.decodeBase37(buf.readLong())
            out.add(RegisterUsernameRequest(username))
        } else if (service == HandshakeMessage.SERVICE_REGISTER_COMMIT) {
            if (state == State.READ_SIZE) {
                if (!buf.isReadable) return

                state = State.READ_PAYLOAD
                size = buf.readUnsignedByte().toInt()
            }

            if (state == State.READ_PAYLOAD) {
                if (buf.readableBytes() < size) return

                val encryptedSize = buf.readUnsignedByte().toInt()
                if (encryptedSize != size - 1) throw IOException("Encrypted size mismatch.")

                val secureBuffer =
                    ByteBufUtils.rsa(buf.readBytes(encryptedSize), RsaKeySet.MODULUS, RsaKeySet.PRIVATE_KEY)
                val encryptedType = secureBuffer.readUnsignedByte().toInt()
                if (encryptedType != 10) throw IOException("Invalid encrypted block type.")

                secureBuffer.readUnsignedShort()
                val version = secureBuffer.readUnsignedShort()
                val username = Base37Utils.decodeBase37(secureBuffer.readLong())
                secureBuffer.readInt()
                val password = ByteBufUtils.readString(secureBuffer)
                secureBuffer.readInt()
                val affiliate = secureBuffer.readUnsignedShort()
                val day = secureBuffer.readUnsignedByte().toInt()
                val month = secureBuffer.readUnsignedByte().toInt()
                secureBuffer.readInt()
                val year = secureBuffer.readUnsignedShort()
                val country = secureBuffer.readUnsignedShort()
                secureBuffer.readInt()

                out.add(
                    RegisterCommitRequest(
                        version,
                        username,
                        password,
                        GregorianCalendar(year, month, day),
                        country
                    )
                )
            }
        }
    }
}
