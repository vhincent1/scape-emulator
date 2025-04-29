package net.scapeemulator.game.net.game

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.msg.RegionChangeMessage
import net.scapeemulator.game.msg.handler.MessageDispatcher
import net.scapeemulator.game.net.Session
import java.io.IOException
import java.util.*

class GameSession(server: GameServer, channel: Channel, private val player: Player) : Session(server, channel) {
    private val dispatcher: MessageDispatcher = server.messageDispatcher
    private val messages: Queue<Message> = ArrayDeque<Message>()

    fun init() {
        player.session = this

        /* set up player for their initial region change */
        val position = player.position
        player.lastKnownRegion = position
        player.send(RegionChangeMessage(player.position))

        /* set up the game interface */
        player.interfaceSet.init()
        player.sendMessage("Welcome to HustlaScape.")

        /* refresh skills, inventory, energy, etc. */
        player.inventory.refresh()
        player.bank.refresh()
        player.equipment.refresh()
        player.settings.refresh()
        player.skillSet.refresh()
        player.energy = player.energy // TODO: nicer way than this?
    }

    @Throws(IOException::class)
    override fun messageReceived(message: Any) {
        synchronized(messages) {
            messages.add(message as Message)
        }
    }

    override fun channelClosed() = server.loginService.addLogoutRequest(player)

    fun send(message: Message): ChannelFuture = channel.write(message)

    fun processMessageQueue() {
        synchronized(messages) {
//            var message: Message? //todo check
//            while (messages.poll().also { message = it } != null)
//                if (message != null) dispatcher.dispatch(player, message)

            for (i in 0 until 10) {
                val message = messages.poll() ?: break
                dispatcher.dispatch(player, message)
            }
            messages.clear()
        }
    }
}
