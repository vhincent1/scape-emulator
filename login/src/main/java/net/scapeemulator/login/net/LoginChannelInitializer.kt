package net.scapeemulator.login.net

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import net.scapeemulator.login.LoginServer
import net.scapeemulator.util.net.LoginFrameDecoder
import net.scapeemulator.util.net.LoginFrameEncoder

class LoginChannelInitializer(private val server: LoginServer) : ChannelInitializer<SocketChannel>() {
    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast(
            ReadTimeoutHandler(5),
            LoginFrameDecoder(),
            LoginFrameEncoder(),
            LoginChannelHandler(server)
        )
    }
}
