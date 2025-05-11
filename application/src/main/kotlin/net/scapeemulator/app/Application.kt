package net.scapeemulator.app

import org.slf4j.LoggerFactory
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provider
import com.google.inject.name.Named
import com.google.inject.name.Names
import dev.misfitlabs.kotlinguice4.KotlinModule
import dev.misfitlabs.kotlinguice4.getInstance
import net.scapeemulator.game.GameServer
import kotlin.system.measureTimeMillis

class Game @Inject constructor() {
    fun start(){

    }
}
object Application {
    private val logger = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        var injector: Injector

        val time = measureTimeMillis {
            injector = Guice.createInjector(ApplicationModule(args))
//            injector.getInstance<GameServer>().main()
         //  injector.getInstance<Game>().start()
//            injector.getInstance<World>().start()
        }

        addShutDownHook(injector)
    }
    private fun addShutDownHook(injector: Injector) {
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.info("Gracefully shutting down the server.")
                injector.getInstance<GameServer>().shutdown()
//                injector.getInstance<Network>().shutdown()
            }
        )
    }
}

class ApplicationModule(val args: Array<String>) : KotlinModule() {
    override fun configure() {
        bind<Array<String>>().annotatedWith(Names.named("args")).toInstance(args)
       //install(GameModule)
        //install(HttpModule)
    }
}
