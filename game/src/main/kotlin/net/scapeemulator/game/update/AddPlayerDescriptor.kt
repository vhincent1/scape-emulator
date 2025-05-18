package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class AddPlayerDescriptor(player: Player, tickets: IntArray) : PlayerDescriptor(player, tickets) {
    private val id: Int
    private val direction: Direction
    private val position: Position

    init {
        this.id = player.index
        this.direction = player.mostRecentDirection
        this.position = player.position
    }

    override fun encodeDescriptor(
        message: PlayerUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        val x = position.x - message.position.x
        val y = position.y - message.position.y

        builder.putBits(11, id)
        builder.putBits(1, if (isBlockUpdatedRequired) 1 else 0) // check
        builder.putBits(5, x)
        builder.putBits(3, direction.toInteger())
        builder.putBits(1, 1) // check isTeleporting()
        builder.putBits(5, y)
    }
}
