package net.scapeemulator.flooder.net

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundByteHandlerAdapter
import net.burtleburtle.bob.rand.IsaacRandom
import net.scapeemulator.util.Base37Utils
import net.scapeemulator.util.ByteBufUtils
import net.scapeemulator.util.crypto.RsaKeySet
import net.scapeemulator.util.crypto.StreamCipher
import java.io.IOException
import java.net.InetSocketAddress
import java.security.SecureRandom

class FlooderChannelHandler(private val crc: IntArray) : ChannelInboundByteHandlerAdapter() {
    private enum class State {
        READ_SERVER_SESSION_KEY,
        READ_LOGIN_STATUS,
        READ_LOGIN_PAYLOAD,
        READ_GAME_OPCODE
    }

    private var state = State.READ_SERVER_SESSION_KEY
    private val clientSessionKey: Long = random.nextLong()
    private var username: String? = null
    private var password: String? = null
    private var serverSessionKey: Long = 0
    private var encodedUsername: Long = 0
    private var inCipher: StreamCipher? = null
    private var outCipher: StreamCipher? = null

    override fun channelActive(ctx: ChannelHandlerContext) {
        val channel = ctx.channel()

        username = "bot" + (channel.localAddress() as InetSocketAddress).port
        //username = "bot"
        password = "password"
        encodedUsername = Base37Utils.encodeBase37(username)

        val packet = ctx.alloc().buffer()
        packet.writeByte(14)
        packet.writeByte(((encodedUsername shr 16) and 31L).toByte().toInt())
        channel.write(packet)
    }

    @Throws(IOException::class)
    public override fun inboundBufferUpdated(ctx: ChannelHandlerContext, buf: ByteBuf) {
        val channel = ctx.channel()

        if (state == State.READ_SERVER_SESSION_KEY) {
            if (buf.readableBytes() >= 9) {
                if (buf.readUnsignedByte().toInt() != 0) throw IOException("expecting EXCHANGE_KEYS opcode")

                serverSessionKey = buf.readLong()

                val payload = ctx.alloc().buffer()
                payload.writeInt(530)

                payload.writeByte(0)
                payload.writeByte(0)
                payload.writeByte(0)

                payload.writeByte(0)

                payload.writeShort(765)
                payload.writeShort(503)

                payload.writeByte(0)

//                val uid = ByteArray(24)
                for (i in 0..22) payload.writeByte(-1)

                ByteBufUtils.writeString(payload, "kKmok3kJqOeN6D3mDdihco3oPeYN2KFy6W5--vZUbNA")

                payload.writeInt(0)
                payload.writeInt(0)
                payload.writeShort(0)

                for (i in 0..28) {
                    payload.writeInt(crc[i])
                }

                var secure = ctx.alloc().buffer()
                secure.writeByte(10)
                secure.writeLong(clientSessionKey)
                secure.writeLong(serverSessionKey)
                secure.writeLong(encodedUsername)
                ByteBufUtils.writeString(secure, password!!)

                secure = ByteBufUtils.rsa(secure, RsaKeySet.MODULUS, RsaKeySet.PUBLIC_KEY)

                payload.writeByte(secure.readableBytes())
                payload.writeBytes(secure)

                val packet = ctx.alloc().buffer()
                packet.writeByte(18)//reconnecting
                packet.writeShort(payload.readableBytes())
                packet.writeBytes(payload)

                channel.write(packet)

                val seed = IntArray(4)
                seed[0] = (clientSessionKey shr 32).toInt()
                seed[1] = clientSessionKey.toInt()
                seed[2] = (serverSessionKey shr 32).toInt()
                seed[3] = serverSessionKey.toInt()
                outCipher = IsaacRandom(seed)
                for (i in seed.indices) seed[i] += 50
                inCipher = IsaacRandom(seed)

                state = State.READ_LOGIN_STATUS
            }
        }

        if (state == State.READ_LOGIN_STATUS) {
            if (buf.isReadable()) {
                val status = buf.readUnsignedByte().toInt()
                if (status != 2) throw IOException("expecting OK login response")

                state = State.READ_LOGIN_PAYLOAD
            }
        }

        if (state == State.READ_LOGIN_PAYLOAD) {
            if (buf.readableBytes() >= 11) {
                buf.readerIndex(buf.readerIndex() + 11)

                state = State.READ_GAME_OPCODE
            }
        }

        if (state == State.READ_GAME_OPCODE) {
        }
    }

    companion object {
        private val random = SecureRandom()
    }
}
