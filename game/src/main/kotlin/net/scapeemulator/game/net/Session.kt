package net.scapeemulator.game.net

import io.netty.channel.Channel
import net.scapeemulator.game.GameServer
import java.io.IOException

abstract class Session(protected val server: GameServer, val channel: Channel) {
    @Throws(IOException::class)
    abstract fun messageReceived(message: Any)

    open fun channelClosed() {
        /* empty */
    }
}
