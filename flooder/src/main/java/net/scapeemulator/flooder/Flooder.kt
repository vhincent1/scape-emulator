package net.scapeemulator.flooder

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.Slf4JLoggerFactory
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.FileStore.Companion.open
import net.scapeemulator.util.NetworkConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.InetSocketAddress

class Flooder {
    private val cache: Cache
    private val bootstrap = Bootstrap()

    init {
        cache = Cache(open("../game/data/cache/"))
        val table = cache.createChecksumTable()
        val crc = IntArray(29)
        for (i in crc.indices)
            crc[i] = table.getEntry(i).crc

        bootstrap.remoteAddress(InetSocketAddress(InetAddress.getLoopbackAddress(), NetworkConstants.GAME_PORT))
        bootstrap.channel(NioSocketChannel::class.java)
        bootstrap.group(NioEventLoopGroup())
        bootstrap.handler(FlooderChannelInitializer(crc))
    }

    @Throws(InterruptedException::class)
    fun start() {
//        for (i in 0..1999)
//        for(i in 0..20)
        for (i in 0..10) bootstrap.connect()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Flooder::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory())
            try {
                val flooder = Flooder()
                flooder.start()
            } catch (t: Throwable) {
                logger.error("Error starting flooder:", t)
            }
        }
    }
}
