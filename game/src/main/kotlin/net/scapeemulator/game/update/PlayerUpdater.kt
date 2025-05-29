//package net.scapeemulator.game.update
//
//import net.scapeemulator.game.model.Npc
//import net.scapeemulator.game.model.Player
//import net.scapeemulator.game.model.World
//import net.scapeemulator.game.msg.NpcUpdateMessage
//import net.scapeemulator.game.msg.PlayerUpdateMessage
//import net.scapeemulator.game.msg.RegionChangeMessage
//import net.scapeemulator.game.msg.ResetMinimapFlagMessage
//import net.scapeemulator.game.update.NpcDescriptor.Companion.create
//import net.scapeemulator.game.update.PlayerDescriptor.Companion.create
//
//class PlayerUpdater(private val world: World) {
//    fun tick() {
//        for (player in world.players) preprocess(player)
//        for (npc in world.npcs) preprocess(npc)
//
//        for (player in world.players) {
//            updatePlayers(player)
//            updateNpcs(player)
//        }
//
//        for (player in world.players) postprocess(player)
//
//        for (npc in world.npcs) postprocess(npc)
//    }
//
//    private fun preprocess(player: Player) {
//        if (player.walkingQueue.isMinimapFlagReset) player.send(ResetMinimapFlagMessage())
//
//        if (isRegionChangeRequired(player)) {
//            val position = player.position
//            player.lastKnownRegion = position
//            player.send(RegionChangeMessage(position))
//        }
//
//        player.walkingQueue.tick()
//    }
//
//    private fun preprocess(npc: Npc) {
//        npc.walkingQueue.tick()
//    }
//
//    private fun updatePlayers(player: Player) {
//        val lastKnownRegion = player.lastKnownRegion
//        val position = player.position
//        val tickets = player.appearanceTickets
//
//        val selfDescriptor: PlayerDescriptor
//        if (player.isTeleporting) selfDescriptor = TeleportPlayerDescriptor(player, tickets)
//        else selfDescriptor = create(player, tickets)
//
//        val descriptors: MutableList<PlayerDescriptor> = ArrayList()
//        val localPlayers = player.localPlayers
//        val localPlayerCount = localPlayers.size
//
//        val it: MutableIterator<Player> = localPlayers.iterator()
//        while (it.hasNext()) {
//            val p = it.next()
//            if (!p.isActive || p.isTeleporting || !position.isWithinDistance(p.position)) {
//                it.remove()
//                descriptors.add(RemovePlayerDescriptor(p, tickets))
//            } else {
//                descriptors.add(create(p, tickets))
//            }
//        }
//
//        for (p in world.players) {
//            if (localPlayers.size >= 255) break
//
//            if (p != player && position.isWithinDistance(p.position) && !localPlayers.contains(p)) {
//                localPlayers.add(p)
//                descriptors.add(AddPlayerDescriptor(p, tickets))
//            }
//        }
////todo check
//        player.send(PlayerUpdateMessage(lastKnownRegion!!, position, localPlayerCount, selfDescriptor, descriptors))
//    }
//
//    private fun updateNpcs(player: Player) {
//        val lastKnownRegion = player.lastKnownRegion
//        val position = player.position
//
//        val descriptors: MutableList<NpcDescriptor> = ArrayList<NpcDescriptor>()
//        val localNpcs = player.localNpcs
//        val localNpcCount = localNpcs.size
//
//        val it: MutableIterator<Npc> = localNpcs.iterator()
//        while (it.hasNext()) {
//            val n = it.next()
//            if (!n.isActive || n.isTeleporting || !position.isWithinDistance(n.position)) {
//                it.remove()
//                descriptors.add(RemoveNpcDescriptor(n))
//            } else {
//                descriptors.add(create(n))
//            }
//        }
//
//        for (n in world.npcs) {
//            if (localNpcs.size >= 255) break
//
//            if (position.isWithinDistance(n.position) && !localNpcs.contains(n)) {
//                localNpcs.add(n)
//                descriptors.add(AddNpcDescriptor(n))
//            }
//        }
//
//        player.send(NpcUpdateMessage(lastKnownRegion!!, position, localNpcCount, descriptors))
//    }
//
//    private fun postprocess(player: Player) {
//        player.reset()
//    }
//
//    private fun postprocess(npc: Npc) {
//        npc.reset()
//    }
//
//    private fun isRegionChangeRequired(player: Player): Boolean {
//        val lastKnownRegion = player.lastKnownRegion
//        val position = player.position
//
//        val deltaX = position.getLocalX(lastKnownRegion!!.centralRegionX)
//        val deltaY = position.getLocalY(lastKnownRegion.centralRegionY)
//
//        return deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY >= 88
//    }
//}
