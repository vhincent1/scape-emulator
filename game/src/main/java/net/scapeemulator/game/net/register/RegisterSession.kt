package net.scapeemulator.game.net.register

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.Session
import java.io.IOException

class RegisterSession(server: GameServer, channel: Channel) : Session(server, channel) {
    @Throws(IOException::class)
    override fun messageReceived(message: Any) {
        channel.write(RegisterResponse(RegisterResponse.STATUS_OK)).addListener(ChannelFutureListener.CLOSE)
    }
}
