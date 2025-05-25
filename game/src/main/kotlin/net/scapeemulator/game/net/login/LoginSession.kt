package net.scapeemulator.game.net.login

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.handler.timeout.ReadTimeoutHandler
import net.burtleburtle.bob.rand.IsaacRandom
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.InterfaceSet
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.net.RsChannelHandler
import net.scapeemulator.game.net.Session
import net.scapeemulator.game.net.game.*
import net.scapeemulator.game.plugin.LoginEvent
import java.io.IOException
import java.security.SecureRandom

class LoginSession(server: GameServer, channel: Channel) : Session(server, channel) {
    private val service: LoginService = server.loginService
    private val serverSessionKey: Long = random.nextLong()
    private var displayMode = 0
    private lateinit var inRandom: IsaacRandom
    private lateinit var outRandom: IsaacRandom

    init {
        init()
    }

    private fun init() {
        val buf = channel.alloc().buffer(8)
        buf.writeLong(serverSessionKey)
        channel.write(LoginResponse(LoginResponse.STATUS_EXCHANGE_KEYS, buf))
    }

    fun sendLoginFailure(status: Int) {
        val response = LoginResponse(status)
        channel.write(response).addListener(ChannelFutureListener.CLOSE)
    }


    //todo cleanup
    fun sendLoginSuccess(status: Int, player: Player) {
        // write login response
        val buf = channel.alloc().buffer(11)
        buf.writeByte(player.rights)
        buf.writeByte(0)
        buf.writeByte(0)
        buf.writeByte(0)
        buf.writeByte(0)
        buf.writeByte(0)
        buf.writeByte(0)
        buf.writeShort(player.index)
        buf.writeByte(1)
        buf.writeByte(1)
        // ---------------------------
        val pipeline = channel.pipeline()
        val session = GameSession(server, channel, player)
        val handler = pipeline.get(RsChannelHandler::class.java)

        handler.session = session
        pipeline.remove(ReadTimeoutHandler::class.java) // TODO a different timeout mechanism is used in-game
        val response = LoginResponse(status, buf)
        channel.write(response)
        pipeline.addFirst(
            GameFrameEncoder(outRandom),
            GameMessageEncoder(server.codecRepository),
            GameFrameDecoder(inRandom),
            GameMessageDecoder(server.codecRepository)
        )

        if (displayMode == 0 || displayMode == 1)
            player.interfaceSet.displayMode = InterfaceSet.DisplayMode.FIXED
        else
            player.interfaceSet.displayMode = InterfaceSet.DisplayMode.RESIZABLE

        player.session = session

//        val account = accountService.findAccountByUsername(username)
//        if (account == null || !reconnecting && !accountService.validateAccount(password, account.password)) {
//            session.writeAndFlush(INVALID_USERNAME_PASSWORD_OPCODE)
//            session.disconnect("Invalid user credentials. Disconnecting client")
//            return
//        }
//        Player(account, world, session)
        //world.requestLogin
        player.login()
        /* plugin event */
        server.plugins.notify(LoginEvent(player))
    }

    @Throws(IOException::class)
    override fun messageReceived(message: Any) {
        val request = message as LoginRequest
        //todo: look at arios isreconnecting
//        println("Reconnecting: "+ request.isReconnecting)
        if (request.serverSessionKey != serverSessionKey) throw IOException("Server session key mismatch.")

        var versionMismatch = false
        if (request.version != server.version) versionMismatch = true

        val table = server.checksumTable
        val crc = request.crc
        for (i in crc.indices) {
            if (table.getEntry(i).crc != crc[i]) {
                versionMismatch = true
                break
            }
        }

        if (versionMismatch) {
            sendLoginFailure(LoginResponse.STATUS_GAME_UPDATED)
            return
        }

        val clientSessionKey = request.clientSessionKey
        val serverSessionKey = request.serverSessionKey
        val seed = IntArray(4)
        seed[0] = (clientSessionKey shr 32).toInt()
        seed[1] = clientSessionKey.toInt()
        seed[2] = (serverSessionKey shr 32).toInt()
        seed[3] = serverSessionKey.toInt()

        inRandom = IsaacRandom(seed)
        for (i in seed.indices) seed[i] += 50
        outRandom = IsaacRandom(seed)
        displayMode = request.displayMode

        service.addLoginRequest(this, request) // --> sendLoginRequest/sendLoginSuccess
    }

    companion object {
        private val random = SecureRandom()
    }
}
