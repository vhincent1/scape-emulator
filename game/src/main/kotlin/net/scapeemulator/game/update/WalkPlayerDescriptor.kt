package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class WalkPlayerDescriptor(player: Player, tickets: IntArray) : PlayerDescriptor(player, tickets) {
    private val direction: Direction

    init {
        this.direction = player.firstDirection
    }

    public override fun encodeDescriptor(
        message: PlayerUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        builder.putBits(1, 1)
        builder.putBits(2, 1)
        builder.putBits(3, direction.toInteger())
        builder.putBits(1, if (isBlockUpdatedRequired) 1 else 0)
    }
}
