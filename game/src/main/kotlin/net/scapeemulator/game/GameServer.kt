package net.scapeemulator.game

import com.github.michaelbull.logging.InlineLogger
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
import net.scapeemulator.game.net.login.LoginService
import net.scapeemulator.game.net.update.UpdateService
import net.scapeemulator.game.util.LandscapeKeyTable
import net.scapeemulator.util.NetworkConstants
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.sql.SQLException
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

class GameServer(loginAddress: SocketAddress) {

    //server socket
    private val executor: ExecutorService = Executors.newCachedThreadPool()

    //game thread
    private val gameExecutor = Executors.newSingleThreadScheduledExecutor()

    val loginService: LoginService
    val updateService: UpdateService

    //
    var world: World
    val version: Int = 530
    val cache: Cache
    val checksumTable: ChecksumTable
    val landscapeKeyTable: LandscapeKeyTable
    val codecRepository: CodecRepository
    val messageDispatcher: MessageDispatcher

    // network
    val network: Network

    init {
        logger.info { "Starting ScapeEmulator game server..." }

        // todo: world list and game settings

        /* load landscape keys */
        landscapeKeyTable = LandscapeKeyTable.open("data/landscape-keys")

        /* load game cache */
        cache = Cache(FileStore.open("data/cache"))
        checksumTable = cache.createChecksumTable()
        ItemDefinitions.init(File("./data/itemDefinitions.json"))
        EquipmentDefinition.init()

//        MapSet.init(cache, landscapeKeyTable);

        /* load message codecs and dispatcher */
        codecRepository = CodecRepository(landscapeKeyTable)
        messageDispatcher = MessageDispatcher()

        /* load player serializer from config file */
        val serializer = createPlayerSerializer()
        logger.info { "Using serializer: $serializer." }
        loginService = LoginService(serializer)
        updateService = UpdateService()

        // load world
        world = World(loginService)

        /* start netty */
        network = Network(this)
        network.start()

        // start server
        start()
    }

    fun start() {
        logger.info { "Ready for connections." }
        world.isOnline = true

        /* start login and update services */
        executor.submit(loginService)
        executor.submit(updateService)

        /* main game tick loop */
        gameExecutor.scheduleAtFixedRate(
            {
                var tick = 0
                if (!world.isOnline) return@scheduleAtFixedRate
                try {
                    val time = measureTimeMillis {
                        ++tick
                        world.tick()
                    }
                    val freeMemoryMB = ((Runtime.getRuntime().freeMemory() / 1024) / 1024).toFloat()
                    val totalMemoryMB = ((Runtime.getRuntime().totalMemory() / 1024) / 1024).toFloat()
                    val maxMemoryMB = ((Runtime.getRuntime().maxMemory() / 1024) / 1024).toFloat()
                    val allocatedMemoryMB = (totalMemoryMB - freeMemoryMB)
                    val freeMemory = (maxMemoryMB - allocatedMemoryMB)
                    val usedPercentage = (allocatedMemoryMB / maxMemoryMB) * 100
                    logger.debug {
                        "Game Tick #$tick took $time ms. Players=${world.players.size} Npcs=${world.npcs.size} " +
                                "Memory=(Max: $maxMemoryMB MB Allocated: $allocatedMemoryMB MB Free: $freeMemory MB Used: $usedPercentage%)"
                    }
                } catch (exception: Exception) {
                    logger.error(exception) { "Error occurred during game tick #$tick" }
                }
            },
            600, 600, TimeUnit.MILLISECONDS
        )
    }

    fun shutdown() {
        world.isOnline = false
        println("bye")
        //network.shutdown() //world.online=false
        //game.shutdown() //save players
    }

    @Throws(IOException::class, SQLException::class)
    private fun createPlayerSerializer(): PlayerSerializer {
        val properties = Properties()

        FileInputStream("data/serializer.conf").use { file ->
            properties.load(file)
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

    companion object {
        private val logger = InlineLogger(GameServer::class)
        lateinit var world: World

        @JvmStatic
        fun main(args: Array<String>) = try {
            val server = GameServer(InetSocketAddress(NetworkConstants.LOGIN_PORT))
            /* shutdown hook */
            Runtime.getRuntime().addShutdownHook(Thread { server.shutdown() })

            //server.start()
            world = server.world
        } catch (t: Throwable) {
            logger.error(t) { "Failed to start server." }
        }
    }

}
