package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.PATHFINDING_ENABLED
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.msg.RegionChangeMessage
import net.scapeemulator.game.msg.ResetMinimapFlagMessage
import net.scapeemulator.game.net.login.LoginService
import net.scapeemulator.game.pathfinder.RegionManager
import net.scapeemulator.game.pathfinder.RegionMapListener
import net.scapeemulator.game.pathfinder.TraversalMap
import net.scapeemulator.game.pathfinder.TraversalMapListener
import net.scapeemulator.game.task.SyncTask
import net.scapeemulator.game.task.TaskScheduler
import net.scapeemulator.game.update.*
import net.scapeemulator.game.update.PlayerDescriptor.Companion.create


//world id
class World(val worldId: Int, private val loginService: LoginService) : SyncTask {
    companion object {
        const val MAX_PLAYERS: Int = 2000
        const val MAX_NPCS: Int = 2000
        const val MAX_GROUND_ITEMS: Int = 2000
    }

    var isOnline = false
    val taskScheduler: TaskScheduler = TaskScheduler()

    val players: ActorList<Player> = ActorList(MAX_PLAYERS)
    val npcs: ActorList<Npc> = ActorList(MAX_NPCS)
    val groundItemManager = GroundItemManager(NodeList(MAX_GROUND_ITEMS))

    val regionManager = RegionManager()
    val traversalMap = TraversalMap(regionManager)

    init {
        if (PATHFINDING_ENABLED) {
            GameServer.INSTANCE.mapSet.apply {
                addListener(RegionMapListener(regionManager))
                addListener(TraversalMapListener(traversalMap))
            }
        }
    }

    override fun sync(tick: Int) {
        if (!isOnline) return
        processLoginRequest()
        for (player in players) {
            if (player == null) continue
            val session = player.session
            session?.processMessageQueue()
        }
        taskScheduler.tick()
        groundItemManager.tick()
        players.preprocessPlayers()
        npcs.preprocessNpcs()
        for (player in players) {
            players.updatePlayers(player)
            npcs.updateNpcs(player)
        }
        players.postprocessPlayers()
        npcs.postprocessNpcs()
    }

    private fun processLoginRequest() {
        if (!isOnline) return
        /*
        * As the MobList class is not thread-safe, players must be registered
        * within the game logic processing code.
         */
        loginService.registerNewPlayers(this)
    }

    private fun processHitQueue(mob: Mob) {
        for (i in 0..2 step 1) {
            if (mob.hitQueue.peek() == null) continue
            val hit = mob.hitQueue.poll()
            val secondary = i == 1
            if (secondary) {
                mob.secondaryHit = hit
                mob.isHit2Updated = true
            } else {
                mob.primaryHit = hit
                mob.isHitUpdated = true
            }
        }
    }

    private fun ActorList<Player>.preprocessPlayers() {
        if (isEmpty()) return
        for (player in this) {
            if (player == null /* || !player.isOnline*/) continue
            if (player.walkingQueue.isMinimapFlagReset) player.send(ResetMinimapFlagMessage())
            if (isRegionChangeRequired(player)) {
                val position = player.position
                player.lastKnownRegion = position
                player.send(RegionChangeMessage(position))
            }
            player.walkingQueue.tick()
            processHitQueue(player)
        }
    }

    private fun ActorList<Npc>.preprocessNpcs() {
        if (isEmpty()) return
        for (npc in this) {
            if (npc == null) continue
            npc.walkingQueue.tick()
            processHitQueue(npc)
        }
    }

    private fun ActorList<Player>.updatePlayers(player: Player?) {
        if (player == null) return
        val lastKnownRegion = player.lastKnownRegion
        val position = player.position
        val tickets = player.appearanceTickets
        val selfDescriptor: PlayerDescriptor =
            if (player.isTeleporting) TeleportPlayerDescriptor(player, tickets)
            else create(player, tickets)
        val descriptors: MutableList<PlayerDescriptor> = ArrayList()
        val localPlayers = player.localPlayers
        val localPlayerCount = localPlayers.size
        val it: MutableIterator<Player> = localPlayers.iterator()
        while (it.hasNext()) {
            val p = it.next()
            if (!p.isActive || p.isTeleporting || !position.isWithinDistance(p.position)) {
                it.remove()
                descriptors.add(RemovePlayerDescriptor(p, tickets))
            } else {
                descriptors.add(create(p, tickets))
            }
        }
        for (p in this) {
            if (p == null) continue
            if (localPlayers.size >= 255) break
            if (p != player && position.isWithinDistance(p.position) && !localPlayers.contains(p)) {
                localPlayers.add(p)
                descriptors.add(AddPlayerDescriptor(p, tickets))
            }
        }
        //todo check
        player.send(PlayerUpdateMessage(lastKnownRegion!!, position, localPlayerCount, selfDescriptor, descriptors))
    }

    private fun ActorList<Npc>.updateNpcs(player: Player?) {
        if (player == null) return
        val lastKnownRegion = player.lastKnownRegion
        val position = player.position
        val descriptors: MutableList<NpcDescriptor> = ArrayList()
        val localNpcs = player.localNpcs
        val localNpcCount = localNpcs.size
        val it: MutableIterator<Npc> = localNpcs.iterator()
        while (it.hasNext()) {
            val n = it.next()
            if (!n.isActive || n.isTeleporting || !position.isWithinDistance(n.position)) {
                it.remove()
                descriptors.add(RemoveNpcDescriptor(n))
            } else {
                descriptors.add(NpcDescriptor.create(n))
            }
        }
        for (n in this) {
            if (n == null) continue
            if (localNpcs.size >= 255) break
            if (position.isWithinDistance(n.position) && !localNpcs.contains(n)) {
                localNpcs.add(n)
                descriptors.add(AddNpcDescriptor(n))
            }
        }
        player.send(NpcUpdateMessage(lastKnownRegion!!, position, localNpcCount, descriptors))
    }

    private fun ActorList<Player>.postprocessPlayers() {
        if (isEmpty()) return
        for (player in this) {
            if (player == null) continue
            player.reset()
        }
    }

    private fun ActorList<Npc>.postprocessNpcs() {
        if (isEmpty()) return
        for (npc in this) {
            if (npc == null) continue
            npc.reset()
        }
    }

    fun getPlayerByName(username: String): Player? {
        for (player in players) {
            if (player == null) continue
            if (player.username == username) return player
        }
        return null
    }

    private fun isRegionChangeRequired(player: Player): Boolean {
        val lastKnownRegion = player.lastKnownRegion
        val position = player.position
        val deltaX = position.getLocalX(lastKnownRegion!!.centralRegionX)
        val deltaY = position.getLocalY(lastKnownRegion.centralRegionY)
        return deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY >= 88
    }

}
