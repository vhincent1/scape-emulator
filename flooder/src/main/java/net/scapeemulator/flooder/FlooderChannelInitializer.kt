package net.scapeemulator.flooder

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import net.scapeemulator.flooder.net.FlooderChannelHandler

class FlooderChannelInitializer(private val crc: IntArray) : ChannelInitializer<SocketChannel>() {
    public override fun initChannel(ch: SocketChannel) {
        ch.pipeline().addLast(FlooderChannelHandler(crc))
    }
}
