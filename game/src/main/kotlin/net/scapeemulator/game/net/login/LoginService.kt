package net.scapeemulator.game.net.login

import net.scapeemulator.game.io.PlayerSerializer
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.World
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque

class LoginService(private val serializer: PlayerSerializer) : Runnable {
    companion object {
        const val LOGIN_REQUESTS = 20
    }

    //todo fix login
    private class SessionPlayerPair(val session: LoginSession, val player: Player)

    private val jobs: BlockingQueue<Job> = LinkedBlockingDeque<Job>()
    private val connectionList = arrayListOf<String>()
    private val newPlayers: Queue<SessionPlayerPair> = ArrayDeque<SessionPlayerPair>()
    private val oldPlayers: Queue<Player> = ArrayDeque<Player>()

    fun addLoginRequest(session: LoginSession, request: LoginRequest) = jobs.add(LoginJob(session, request))
    fun addLogoutRequest(player: Player) = synchronized(oldPlayers) { oldPlayers.add(player) }

    private fun processLoginRequests(world: World) = synchronized(newPlayers) {
        if (newPlayers.isEmpty()) return@synchronized
        newPlayers.take(LOGIN_REQUESTS).onEach { pair ->
            if (world.getPlayerByName(pair.player.username) != null) {
                /* player is already online */
                pair.session.sendLoginFailure(LoginResponse.STATUS_ALREADY_ONLINE)
            } else if (!world.players.add(pair.player)) { // add player
                /* world is full */
                pair.session.sendLoginFailure(LoginResponse.STATUS_WORLD_FULL)
            } else {
                /* send success packet & switch to GameSession */
                pair.session.sendLoginSuccess(LoginResponse.STATUS_OK, pair.player)
            }
        }.also(newPlayers::removeAll)

//            var pair: SessionPlayerPair?
//            while (newPlayers.poll().also { pair = it } != null) {
//                if (pair == null) return
//                // todo: ip address check
////                println("new player " + pair.player.username)
////                println("ADDRESS: ${pair.session.channel.remoteAddress()}")
////                val ip = pair.session.channel.remoteAddress() as InetSocketAddress
////                val ipAddress = ip.address.toString()
////                println(ipAddress)
////
////                val c = connectionList.count { it.contains(ipAddress) }
////                if (c >= 3) {
////                    pair.session.sendLoginFailure(LoginResponse.STATUS_LOGIN_LIMIT_EXCEEDED)
////                    return
////                } else {
////                    connectionList.add(ipAddress)
////                }
////                println("count "+c)
//
//                if (world.getPlayerByName(pair.player.username) != null) {
//                    /* player is already online */
//                    pair.session.sendLoginFailure(LoginResponse.STATUS_ALREADY_ONLINE)
//                } else if (!world.players.add(pair.player)) {
//                    /* world is full */
//                    pair.session.sendLoginFailure(LoginResponse.STATUS_WORLD_FULL)
//                } else {
//                    /* send success packet & switch to GameSession */
//                    pair.session.sendLoginSuccess(LoginResponse.STATUS_OK, pair.player)
//                }
//            }
//        }
    }

    private fun processLogoutRequests(world: World) = synchronized(oldPlayers) {
//        var player: Player?//todo check
//        while (oldPlayers.poll().also { player = it } != null) {
//            if (player == null) continue
//            val rem = world.players.remove(player)//check
//            player.resetId()
//            println("Remove $rem index=${player.index}")
////                connectionList.remove((player.session.channel.remoteAddress() as InetSocketAddress).address.toString())
//            jobs.add(LogoutJob(player))
//        }

        if (oldPlayers.isEmpty()) return@synchronized
        oldPlayers.onEach { player ->
            val rem = world.players.remove(player)//check
            player.resetId()
//            println("Remove $rem index=${player.index}")
//                connectionList.remove((player.session.channel.remoteAddress() as InetSocketAddress).address.toString())
            jobs.add(LogoutJob(player))
        }
        oldPlayers.clear()
    }

    fun registerNewPlayers(world: World) {
        if (!world.isOnline) {
            println("World is offline")
            return
        }
        processLogoutRequests(world)
        processLoginRequests(world)
    }

    override fun run() {
        while (true) {
            try {
                jobs.take().perform(this)
            } catch (e: InterruptedException) {
                /* ignore */
            }
        }
    }

    private abstract class Job {
        abstract fun perform(service: LoginService)
    }

    private class LoginJob(private val session: LoginSession, private val request: LoginRequest) : Job() {
        override fun perform(service: LoginService) {
            val result = service.serializer.load(request.username, request.password)
            val status = result.status

            if (status != LoginResponse.STATUS_OK) {
                /* send failure response immediately */
                session.sendLoginFailure(status)
            } else {
                /* add to new player queue */
                synchronized(service.newPlayers) {
                    service.newPlayers.add(SessionPlayerPair(session, result.player!!))
                }
            }
        }
    }

    private class LogoutJob(private val player: Player) : Job() {
        override fun perform(service: LoginService) {
            service.serializer.save(player)
        }
    }
}
