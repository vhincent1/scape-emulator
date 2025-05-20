package net.scapeemulator.game

import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.ChecksumTable
import net.scapeemulator.cache.FileStore
import net.scapeemulator.game.cache.LandscapeKeyTable
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
import net.scapeemulator.game.plugin.PluginManager
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

    // server socket
    private val executor: ExecutorService = Executors.newCachedThreadPool()

    // io
    private val executorIo: ExecutorService = Executors.newSingleThreadExecutor()

    // game thread
    private val gameExecutor = Executors.newSingleThreadScheduledExecutor()

    val loginService: LoginService
    val updateService: UpdateService

    // world
    val world: World
    val version: Int = 530

    val cache: Cache
    val checksumTable: ChecksumTable
    val landscapeKeyTable: LandscapeKeyTable
    val codecRepository: CodecRepository
    val messageDispatcher: MessageDispatcher
    val plugins: PluginManager


    // network
    val network: Network

    init {
        logger.info { "Starting ScapeEmulator game server..." }

        // todo: world list and game settings

        /* load landscape keys */
//        println("Directory: "+System.getProperty("user.dir"))
        landscapeKeyTable = LandscapeKeyTable.open("data/landscape-keys")

        /* load game cache */
        cache = Cache(FileStore.open("data/cache"))
        checksumTable = cache.createChecksumTable()
//        MapSet.init(cache, landscapeKeyTable)

        /* load item definitions */
        ItemDefinitions.init(File("./data/itemDefinitions.json"))
        EquipmentDefinition.init()

        /* load message codecs and dispatcher */
        codecRepository = CodecRepository(landscapeKeyTable)
        messageDispatcher = MessageDispatcher()

        /* load player serializer from config file */
        val serializer = createPlayerSerializer()
        logger.info { "Using serializer: $serializer." }
        loginService = LoginService(serializer)
        updateService = UpdateService()


        /* load world */
        world = World(1, loginService)

        /* load plugins */
        plugins = PluginManager(this)

        /* start netty */
        network = Network(this)
        network.start()
    }

    private fun start() {
        world.isOnline = true

        /* start login and update services */
        executor.submit(loginService)
        executor.submit(updateService)

        /* i/o services */
//        executorIo.submit {
//            /* load plugins */
//            val plugins = ScriptManager()
//            plugins.load(File("./data/scripts/"))
//        }

        /* main game tick loop */

        var tick = 0
        gameExecutor.scheduleAtFixedRate(
            {
                if (!world.isOnline) return@scheduleAtFixedRate

                try {
                    val time = measureTimeMillis {
                        ++tick
                        world.sync(tick)
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

    private fun shutdown() {
//        world.isOnline = false

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
        private val logger = KotlinLogging.logger {}
        val INSTANCE: GameServer = GameServer(InetSocketAddress(NetworkConstants.LOGIN_PORT))
        //   val plugins: PluginManager = PluginManager(INSTANCE)

        @JvmStatic
        fun main(args: Array<String>) = try {
            /* start server */
            INSTANCE.start()
//            PluginManager(INSTANCE)
            /* shutdown hook */
            Runtime.getRuntime().addShutdownHook(Thread { INSTANCE.shutdown() })
        } catch (t: Throwable) {
            logger.error(t) { "Failed to start server." }
        }
    }
}
