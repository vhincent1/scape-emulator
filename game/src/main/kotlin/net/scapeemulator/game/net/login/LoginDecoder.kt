package net.scapeemulator.game.net.login

import io.netty.buffer.ByteBuf
import io.netty.buffer.MessageBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import net.scapeemulator.util.Base37Utils
import net.scapeemulator.util.ByteBufUtils
import net.scapeemulator.util.crypto.RsaKeySet
import java.io.IOException

class LoginDecoder : ByteToMessageDecoder() {
    private enum class State {
        READ_HEADER, READ_PAYLOAD
    }

    private var hash = 0
    private var type = 0
    private var size = 0
    private var state = State.READ_HEADER

    @Throws(IOException::class)
    public override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MessageBuf<Any>) {
        when (state) {
            State.READ_HEADER -> {
                if (buf.readableBytes() < 4) return

                state = State.READ_PAYLOAD
                hash = buf.readUnsignedByte().toInt()
                type = buf.readUnsignedByte().toInt()
                size = buf.readUnsignedShort()

                if (type != 16 && type != 18) throw IOException("Invalid login type.")
            }

            State.READ_PAYLOAD -> {
                if (buf.readableBytes() < size) return

                val version = buf.readInt()

                buf.readUnsignedByte()
                buf.readUnsignedByte()
                buf.readUnsignedByte()

                val displayMode = buf.readUnsignedByte().toInt() // 0 = sd, 1 = hd small, 2 = hd resizable

                val width = buf.readUnsignedShort()
                val height = buf.readUnsignedShort()

                val uid = ByteArray(25)//24
                for (i in uid.indices) uid[i] = buf.readByte()

                val settings = ByteBufUtils.readString(buf)

                val affiliate = buf.readInt()
                val flags = buf.readInt()

                buf.readShort() //interface packet counter

                val crc = IntArray(29) //28
                for (i in crc.indices)
                    crc[i] = buf.readInt()

                val encryptedSize = buf.readUnsignedByte().toInt()
                val secureBuffer = ByteBufUtils.rsa(
                    buf.readBytes(encryptedSize),
                    RsaKeySet.MODULUS, RsaKeySet.PRIVATE_KEY
                )

                val encryptedType = secureBuffer.readUnsignedByte().toInt()
                if (encryptedType != 10) throw IOException("Invalid encrypted block type.")

                val clientSessionKey = secureBuffer.readLong()
                val serverSessionKey = secureBuffer.readLong()

                val encodedUsername = secureBuffer.readLong()
                val username = Base37Utils.decodeBase37(encodedUsername)
                val password = ByteBufUtils.readString(secureBuffer)

                if (((encodedUsername shr 16) and 31L) != hash.toLong()) throw IOException("Username hash mismatch.")

//            			int[] seed = new int[4];
//			for (int i = 0; i < 4; i++) {
//				seed[i] = secureBuffer.readInt();
//			}
//			int seed[] = {
//					(int) (clientSessionKey >> 32),
//					(int) clientSessionKey,
//					(int) (serverSessionKey >> 32),
//					(int) serverSessionKey
//			};
                val reconnecting = type == 16 // reconnect world login
                // 18 World Login
                println("Type: $type")
                out.add(
                    LoginRequest(
                        reconnecting,
                        username, password,
                        clientSessionKey, serverSessionKey,
                        version, crc, displayMode
                    )
                )
                ctx.pipeline().remove(this)
            }
        }
    }

    companion object {
        fun getISAACSeed(buffer: ByteBuf): IntArray {
            val seed = IntArray(4)
            for (i in 0..3) {
                seed[i] = buffer.readInt()
            }
            return seed
        }
    }
}
