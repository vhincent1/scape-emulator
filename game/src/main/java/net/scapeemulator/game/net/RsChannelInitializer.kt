package net.scapeemulator.game.net

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.handshake.HandshakeDecoder

class RsChannelInitializer(private val server: GameServer) : ChannelInitializer<SocketChannel>() {
    public override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast(
            ReadTimeoutHandler(5),
            HandshakeDecoder(),
            RsChannelHandler(server)
        )
    }
}
