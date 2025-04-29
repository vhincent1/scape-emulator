package net.scapeemulator.game.net.jaggrab

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.FileRegion
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.Session
import net.scapeemulator.game.net.file.FileProvider
import java.io.IOException

class JaggrabSession(server: GameServer, channel: Channel) : Session(server, channel) {
    @Throws(IOException::class)
    override fun messageReceived(message: Any) {
        val request = message as JaggrabRequest
        val file: FileRegion? = provider.serve(request.path)
        if (file != null) {
            channel.write(file).addListener(ChannelFutureListener.CLOSE)
        } else {
            channel.close()
        }
    }

    companion object {
        private val provider = FileProvider(true)
    }
}
