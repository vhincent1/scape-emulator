package net.scapeemulator.login.net

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundMessageHandlerAdapter
import net.scapeemulator.login.LoginServer
import net.scapeemulator.util.net.LoginFrame
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LoginChannelHandler(private val server: LoginServer) : ChannelInboundMessageHandlerAdapter<LoginFrame>() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info("Channel connected: " + ctx.channel().remoteAddress() + ".")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        logger.info("Channel disconnected: " + ctx.channel().remoteAddress() + ".")
    }

    override fun messageReceived(ctx: ChannelHandlerContext, msg: LoginFrame) {
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LoginChannelHandler::class.java)
    }
}
