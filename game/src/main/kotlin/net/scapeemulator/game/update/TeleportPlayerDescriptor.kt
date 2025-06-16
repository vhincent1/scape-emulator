package net.scapeemulator.game.update

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class TeleportPlayerDescriptor(val player: Player, tickets: IntArray) : PlayerDescriptor(player, tickets) {
    private val regionChanging: Boolean = player.isRegionChanging

    override fun encodeDescriptor(
        message: PlayerUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        val lastKnownRegion = message.lastKnownRegion
        val position = message.position

        val x = position.getLocalX(lastKnownRegion.centralRegionX)
        val y = position.getLocalY(lastKnownRegion.centralRegionY)
        val height = position.height

        builder.putBits(1, 1)
        builder.putBits(2, 3)
        builder.putBits(7, y)
        builder.putBits(1, if (regionChanging) 0 else 1)
        builder.putBits(2, height)
        builder.putBits(1, if (isBlockUpdatedRequired) 1 else 0)
        builder.putBits(7, x)
    }
}
