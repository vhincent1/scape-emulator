package net.scapeemulator.game.net.world

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.net.Session
class WorldListSession(server: GameServer, channel: Channel) : Session(server, channel) {


    data class  WorldDefinition(
        val worldId: Int,
        val location: Int,
        val flag: Int,
        val activity: String,
        val ip: String,
        val region: String,
        val country: Int
    )

    override fun messageReceived(message: Any) {
//todo fix
        val handshake = message as WorldHandshakeMessage
        println("message received: ${handshake.sessionId}")

        //fetch through a database or whatever

        val worlds = arrayOf<World>(
            World(1, World.FLAG_MEMBERS or World.FLAG_HIGHLIGHT, 0, "-", "127.0.0.1"),
            World(2, World.FLAG_MEMBERS or World.FLAG_HIGHLIGHT, 0, "a", "127.0.0.1"),
//            World(2, World.FLAG_MEMBERS or World.FLAG_HIGHLIGHT, 0, "-", "127.0.0.1"),
//            World(2, World.FLAG_MEMBERS or World.FLAG_LOOT_SHARE, 0, "a", "127.0.0.1")
        )
        val players = intArrayOf(1,2,3) //todo fix
//        server.world.players.count()
        channel.write(WorldListMessage(-0x21524111, COUNTRIES, worlds, players))
            .addListener(ChannelFutureListener.CLOSE)
    }

    companion object {
        private val COUNTRIES = arrayOf<Country>(
//            Country(Country.FLAG_UK, "UK"),
//            Country(Country.FLAG_USA, "USA"),
            Country(Country.FLAG_BELGIUM, "Germany"),
//            Country(Country.FLAG_BELGIUM, "Germany"),
        )
    }
}
