package net.scapeemulator.game.model

import net.scapeemulator.game.net.world.WorldListSession
import net.scapeemulator.game.task.TaskScheduler
import net.scapeemulator.game.update.PlayerUpdater

class World private constructor() {
    @JvmField
    val players: MobList<Player> = MobList<Player>(MAX_PLAYERS)
    @JvmField
    val npcs: MobList<Npc> = MobList<Npc>(32000)
    val taskScheduler: TaskScheduler = TaskScheduler()
    private val updater = PlayerUpdater(this)

    fun tick() {
        for (player in players) {
            val session = player.session
            session?.processMessageQueue()
        }

        taskScheduler.tick()
        updater.tick()
    }

    fun getPlayerByName(username: String): Player? {
        for (player in players) {
            if (player.username == username) return player
        }

        return null
    }

    companion object {
        const val MAX_PLAYERS: Int = 2000

        val world: World = World()
    }
}
