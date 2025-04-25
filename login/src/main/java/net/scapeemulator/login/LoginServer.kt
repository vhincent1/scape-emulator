package net.scapeemulator.login

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.Slf4JLoggerFactory
import net.scapeemulator.login.net.LoginChannelInitializer
import net.scapeemulator.util.NetworkConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.net.SocketAddress

class LoginServer {
    private val bootstrap = ServerBootstrap()

    init {
        logger.info("Starting ScapeEmulator login server...")
        bootstrap.group(NioEventLoopGroup())
        bootstrap.channel(NioServerSocketChannel::class.java)
        bootstrap.childHandler(LoginChannelInitializer(this))
    }

    @Throws(InterruptedException::class)
    fun bind(address: SocketAddress) {
        logger.info("Binding to address: $address...")
        bootstrap.localAddress(address).bind().sync()
    }

    fun start() {
        logger.info("Ready for connections.")
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LoginServer::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory())

                val server = LoginServer()
                server.bind(InetSocketAddress(NetworkConstants.LOGIN_PORT))
                server.start()
            } catch (t: Throwable) {
                logger.error("Failed to start server.", t)
            }
        }
    }
}
