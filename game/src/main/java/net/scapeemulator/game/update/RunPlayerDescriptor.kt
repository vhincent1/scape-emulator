package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class RunPlayerDescriptor(player: Player, tickets: IntArray) : PlayerDescriptor(player, tickets) {
    private val firstDirection: Direction
    private val secondDirection: Direction

    init {
        this.firstDirection = player.firstDirection
        this.secondDirection = player.secondDirection
    }

    public override fun encodeDescriptor(
        message: PlayerUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        builder.putBits(1, 1)
        builder.putBits(2, 2)
        builder.putBits(1, 1) // TODO what is this?
        builder.putBits(3, firstDirection.toInteger())
        builder.putBits(3, secondDirection.toInteger())
        builder.putBits(1, if (isBlockUpdatedRequired) 1 else 0)
    }
}
