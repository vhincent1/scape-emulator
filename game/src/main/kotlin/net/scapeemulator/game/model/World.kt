package net.scapeemulator.game.model

import net.scapeemulator.game.net.login.LoginService
import net.scapeemulator.game.task.TaskScheduler
import net.scapeemulator.game.update.PlayerUpdater

//world id
class World(val loginService: LoginService) {
    var isOnline = false

    val players: MobList<Player> = MobList(MAX_PLAYERS)
    val npcs: MobList<Npc> = MobList(MAX_NPCS)
    val taskScheduler: TaskScheduler = TaskScheduler()

    private val updater = PlayerUpdater(this)

    fun tick() {
        if (!isOnline) return
        processLoginRequest()

        for (player in players) {
            val session = player.session
            session.processMessageQueue()
        }

        taskScheduler.tick()
        updater.tick()
    }

    fun processLoginRequest() {
        if (!isOnline) return
        /*
        * As the MobList class is not thread-safe, players must be registered
        * within the game logic processing code.
         */
        loginService.registerNewPlayers(this)
    }

    fun getPlayerByName(username: String): Player? {
        for (player in players) {
            if (player.username == username) return player
        }
        return null
    }

    fun spawnNPC() {
        npcs.add(Npc(1).apply {
            id = 1
            position = Position(3200, 3200)
        })
    }

    companion object {
        const val MAX_PLAYERS: Int = 2000
        const val MAX_NPCS: Int = 2000

//        val world: World = World()
    }
}
