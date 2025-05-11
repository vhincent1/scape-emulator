package net.scapeemulator.game.net

import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundMessageHandlerAdapter
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.auto.AutoLoginSession
import net.scapeemulator.game.net.handshake.HandshakeMessage
import net.scapeemulator.game.net.jaggrab.JaggrabSession
import net.scapeemulator.game.net.login.LoginSession
import net.scapeemulator.game.net.register.RegisterSession
import net.scapeemulator.game.net.update.UpdateSession
import net.scapeemulator.game.net.world.WorldListSession
import java.io.IOException

class RsChannelHandler(private val server: GameServer) : ChannelInboundMessageHandlerAdapter<Any>() {
    private var session: Session? = null

    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info { "Channel connected: " + ctx.channel().remoteAddress() + "." }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        if (session != null)
            session?.channelClosed()
        logger.info { "Channel disconnected: " + ctx.channel().remoteAddress() + "." }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        //TODO fix
        logger.warn(cause) { "Exception caught, closing channel..." }
        ctx.close()
    }

    @Throws(IOException::class)
    override fun messageReceived(ctx: ChannelHandlerContext, message: Any) {
        if (session != null) {
            session?.messageReceived(message)
        } else {
            val handshake = message as HandshakeMessage
            when (handshake.service) {
                HandshakeMessage.SERVICE_LOGIN ->
                    session = LoginSession(server, ctx.channel())

                HandshakeMessage.SERVICE_UPDATE ->
                    session = UpdateSession(server, ctx.channel())

                HandshakeMessage.SERVICE_JAGGRAB ->
                    session = JaggrabSession(server, ctx.channel())

                HandshakeMessage.SERVICE_REGISTER_PERSONAL_DETAILS,
                HandshakeMessage.SERVICE_REGISTER_USERNAME,
                HandshakeMessage.SERVICE_REGISTER_COMMIT ->
                    session = RegisterSession(server, ctx.channel())

                HandshakeMessage.SERVICE_AUTO_LOGIN ->
                    session = AutoLoginSession(server, ctx.channel())

                HandshakeMessage.SERVICE_WORLD_LIST ->
                    session = WorldListSession(server, ctx.channel())
            }
        }
    }

    fun setSession(session: Session) {
        this.session = session
    }

    companion object {
        /* TODO: more specific generics here? */
        private val logger = KotlinLogging.logger { }
    }
}