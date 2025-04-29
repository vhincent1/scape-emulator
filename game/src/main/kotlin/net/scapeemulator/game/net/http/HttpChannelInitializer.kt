package net.scapeemulator.game.net.http

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.timeout.ReadTimeoutHandler

class HttpChannelInitializer : ChannelInitializer<SocketChannel>() {
    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast(
            ReadTimeoutHandler(5),
            HttpRequestDecoder(),
            HttpResponseEncoder(),
            HttpObjectAggregator(1024),
            HttpChannelHandler()
        )
    }
}
