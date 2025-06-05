package net.scapeemulator.game.net.register

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.Session
import java.io.IOException

class RegisterSession(server: GameServer, channel: Channel) : Session(server, channel) {
    @Throws(IOException::class)
    override fun messageReceived(message: Any) {
//        val message = message as RegisterPersonalDetailsRequest
        when (message) {
            is RegisterPersonalDetailsRequest -> {
                println("details : ${message.country}")
            }

            is RegisterUsernameRequest -> {
                println("register username ${message.username}")
                val regex = "^[A-Za-z]*$".toRegex()
                if (message.username.matches(regex)) {
//                    println("Username: ${pair.player.username}")
                }
            }

            is RegisterCommitRequest -> {
                println("req: " + message)
            }

            else -> {
                println(message)
                channel.write(RegisterResponse(RegisterResponse.STATUS_OK)).addListener(ChannelFutureListener.CLOSE)
            }
        }
    }
}
