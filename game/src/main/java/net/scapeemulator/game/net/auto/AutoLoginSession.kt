package net.scapeemulator.game.net.auto

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.Session
import net.scapeemulator.game.net.login.LoginResponse
import java.io.IOException

class AutoLoginSession(server: GameServer, channel: Channel) : Session(server, channel) {
    @Throws(IOException::class)
    override fun messageReceived(message: Any) {
        val payload = channel.alloc().buffer(2)
        payload.writeShort(1)
        val response = LoginResponse(LoginResponse.STATUS_SWITCH_WORLD_AND_RETRY, payload)
        channel.write(response).addListener(ChannelFutureListener.CLOSE)
    }
}
