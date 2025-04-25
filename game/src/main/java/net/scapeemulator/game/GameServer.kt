package net.scapeemulator.game

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.Slf4JLoggerFactory
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.ChecksumTable
import net.scapeemulator.cache.FileStore
import net.scapeemulator.game.io.DummyPlayerSerializer
import net.scapeemulator.game.io.JdbcPlayerSerializer
import net.scapeemulator.game.io.PlayerSerializer
import net.scapeemulator.game.model.EquipmentDefinition
import net.scapeemulator.game.model.ItemDefinitions
import net.scapeemulator.game.model.World
import net.scapeemulator.game.msg.codec.CodecRepository
import net.scapeemulator.game.msg.handler.MessageDispatcher
import net.scapeemulator.game.net.http.HttpChannelInitializer
import net.scapeemulator.game.net.RsChannelInitializer
import net.scapeemulator.game.net.login.LoginService
import net.scapeemulator.game.net.update.UpdateService
import net.scapeemulator.game.util.LandscapeKeyTable
import net.scapeemulator.util.NetworkConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.sql.SQLException
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GameServer(loginAddress: SocketAddress) {
    val world: World = World.world
    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private val loopGroup: EventLoopGroup = NioEventLoopGroup()
    private val serviceBootstrap = ServerBootstrap()
    private val httpBootstrap = ServerBootstrap()

    val loginService: LoginService
    val updateService: UpdateService = UpdateService()
    val cache: Cache
    val checksumTable: ChecksumTable
    val landscapeKeyTable: LandscapeKeyTable
    val codecRepository: CodecRepository
    val messageDispatcher: MessageDispatcher
    val version: Int = 530

    init {
        logger.info("Starting ScapeEmulator game server...")

        // todo: world list and game settings

        /* load landscape keys */
        landscapeKeyTable = LandscapeKeyTable.open("data/landscape-keys")

        /* load game cache */
        cache = Cache(FileStore.open("data/cache"))
        checksumTable = cache.createChecksumTable()
        ItemDefinitions.init(cache)
        EquipmentDefinition.init()

//        MapSet.init(cache, landscapeKeyTable);

        /* load message codecs and dispatcher */
        codecRepository = CodecRepository(landscapeKeyTable)
        messageDispatcher = MessageDispatcher()

        /* load player serializer from config file */
        val serializer = createPlayerSerializer()
        logger.info("Using serializer: $serializer.")
        loginService = LoginService(serializer)

        /* start netty */
        httpBootstrap.group(loopGroup)
        httpBootstrap.channel(NioServerSocketChannel::class.java)
        httpBootstrap.childHandler(HttpChannelInitializer())
        serviceBootstrap.group(loopGroup)
        serviceBootstrap.channel(NioServerSocketChannel::class.java)
        serviceBootstrap.childHandler(RsChannelInitializer(this))
    }

    @Throws(IOException::class, SQLException::class)
    private fun createPlayerSerializer(): PlayerSerializer {
        val properties = Properties()
        FileInputStream("data/serializer.conf").use { `is` ->
            properties.load(`is`)
        }
        val type = properties["type"] as String?
        when (type) {
            "dummy" -> return DummyPlayerSerializer()

            "jdbc" -> {
                val url = properties["url"] as String
                val username = properties["username"] as String
                val password = properties["password"] as String
                return JdbcPlayerSerializer(url, username, password)
            }

            else -> throw IOException("unknown serializer type")
        }
    }

    @Throws(InterruptedException::class)
    fun httpBind(address: SocketAddress) {
        logger.info("Binding to HTTP address: $address...")
        httpBootstrap.localAddress(address).bind().sync()
    }

    @Throws(InterruptedException::class)
    fun serviceBind(address: SocketAddress) {
        logger.info("Binding to service address: $address...")
        serviceBootstrap.localAddress(address).bind().sync()
    }

    fun start() {
        logger.info("Ready for connections.")

        /* start login and update services */
        executor.submit(loginService)
        executor.submit(updateService)

        /* main game tick loop */
        while (true) {
            val start = System.currentTimeMillis()
            tick()
            val elapsed = (System.currentTimeMillis() - start)//updateStamp
            val waitFor = 600 - elapsed
            if (waitFor >= 0) {
                try {
                    Thread.sleep(waitFor)
                } catch (e: InterruptedException) {
                    /* ignore */
                }
            }
        }
    }

    private fun tick() {
        /*
		 * As the MobList class is not thread-safe, players must be registered
		 * within the game logic processing code.
		 */
        loginService.registerNewPlayers(world)

        world.tick()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GameServer::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory())

                val server = GameServer(InetSocketAddress(NetworkConstants.LOGIN_PORT))
                try {
                    server.httpBind(InetSocketAddress(NetworkConstants.HTTP_PORT))
                } catch (t: Throwable) {
                    /* TODO: fix Netty's diagnostic message in this case */
                    logger.warn("Failed to bind to HTTP port.", t)
                }
                server.httpBind(InetSocketAddress(NetworkConstants.HTTP_ALT_PORT))

                try {
                    server.serviceBind(InetSocketAddress(NetworkConstants.SSL_PORT))
                } catch (t: Throwable) {
                    /* TODO: fix Netty's diagnostic message in this case */
                    logger.warn("Failed to bind to SSL port.", t)
                }
                server.serviceBind(InetSocketAddress(NetworkConstants.GAME_PORT))

                server.start()
            } catch (t: Throwable) {
                logger.error("Failed to start server.", t)
            }
        }
    }
}
