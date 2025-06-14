package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RegionChangedMessage

private val REGION_CHANGED_MESSAGE = RegionChangedMessage()
internal val RegionChangedMessageDecoder = MessageDecoder(110) { frame ->
//    val buffer: org.arios.net.packet.IoBuffer = org.arios.net.packet.IoBuffer(110)
//    val l: org.arios.game.world.map.Location = context.getLocation()
//    val player: org.arios.game.node.entity.player.Player = context.getPlayer()
//    var flag: Int = l.getZ() shl 1
//    if (context.isTeleport()) {
//        flag = flag or 0x1
//    }
//    buffer.putS(flag)
//    buffer.put(l.getSceneX(player.getPlayerFlags().getLastSceneGraph()))
//    buffer.putA(l.getSceneY(player.getPlayerFlags().getLastSceneGraph()))
    return@MessageDecoder REGION_CHANGED_MESSAGE
}
