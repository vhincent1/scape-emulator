package net.scapeemulator.game

import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import net.scapeemulator.game.net.RsChannelInitializer
import net.scapeemulator.game.net.http.HttpChannelInitializer
import net.scapeemulator.util.NetworkConstants
import java.net.InetSocketAddress
import java.net.SocketAddress

class Network(gameServer: GameServer) {
    companion object {
        val logger = KotlinLogging.logger {}
    }

    private val loopGroup: EventLoopGroup = NioEventLoopGroup()
    private val serviceBootstrap = ServerBootstrap()
    private val httpBootstrap = ServerBootstrap()

    init {
        httpBootstrap.group(loopGroup)
        httpBootstrap.channel(NioServerSocketChannel::class.java)
        httpBootstrap.childHandler(HttpChannelInitializer())

        serviceBootstrap.group(loopGroup)
        serviceBootstrap.channel(NioServerSocketChannel::class.java)
        serviceBootstrap.childHandler(RsChannelInitializer(gameServer))
    }

    fun start() {
        httpBind(InetSocketAddress(NetworkConstants.HTTP_PORT))
        httpBind(InetSocketAddress(NetworkConstants.HTTP_ALT_PORT))
        serviceBind(InetSocketAddress(NetworkConstants.SSL_PORT))
        serviceBind(InetSocketAddress(NetworkConstants.GAME_PORT))
        logger.info { "Ready for connections." }
    }

    @Throws(InterruptedException::class)
    fun httpBind(address: SocketAddress) {
        try {
            logger.info { "Binding to HTTP address: $address..." }
            httpBootstrap.localAddress(address).bind().sync()
        } catch (ex: InterruptedException) {
            logger.warn(ex) { "Failed to bind to HTTP address" }
        }
    }

    @Throws(InterruptedException::class)
    fun serviceBind(address: SocketAddress) {
        try {
            logger.info { "Binding to service address: $address..." }
            serviceBootstrap.localAddress(address).bind().sync()
        } catch (ex: InterruptedException) {
            /* TODO: fix Netty's diagnostic message in this case */
            logger.warn(ex) { "Failed to bind to SSL port." }
        }
    }

}