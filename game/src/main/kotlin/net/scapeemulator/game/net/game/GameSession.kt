package net.scapeemulator.game.net.game

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.msg.handler.MessageDispatcher
import net.scapeemulator.game.net.Session
import net.scapeemulator.game.plugin.MessageEvent
import java.io.IOException
import java.util.*

class GameSession(server: GameServer, channel: Channel, private val player: Player) : Session(server, channel) {
    private val dispatcher: MessageDispatcher = server.messageDispatcher
    private val messages: Queue<Message> = ArrayDeque<Message>()

//     fun login() {
//        player.session = this
//        player.login()
//        /* plugin event */
//        GameServer.plugins.notify(LoginEvent(player))
//    }

    @Throws(IOException::class)
    override fun messageReceived(message: Any) {
        synchronized(messages) {
            messages.add(message as Message)
        }
    }

    override fun channelClosed() {
        server.loginService.addLogoutRequest(player)
    }

    fun send(message: Message): ChannelFuture = channel.write(message)

    fun processMessageQueue() {
        synchronized(messages) {

//            var message: Message? //todo check
//            while (messages.poll().also { message = it } != null)
//                if (message != null) dispatcher.dispatch(player, message)

            for (i in 0 until 10) {
                val message = messages.poll() ?: break
                dispatcher.dispatch(player, message)
                //todo a better way?
                server.plugins.notify(MessageEvent(player, message))
            }
            messages.clear()
        }
    }
}