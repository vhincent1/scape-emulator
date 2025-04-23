package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class AddNpcDescriptor(npc: Npc) : NpcDescriptor(npc) {
    private val id: Int
    private val type: Int
    private val direction: Direction
    private val position: Position

    init {
        this.id = npc.id
        this.type = npc.type
        this.direction = npc.mostRecentDirection
        this.position = npc.position
    }

    public override fun encodeDescriptor(
        message: NpcUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        val x = position.x - message.position.x
        val y = position.y - message.position.y

        builder.putBits(15, id)
        builder.putBits(1, 1) // check
        builder.putBits(3, direction.toInteger())
        builder.putBits(1, if (isBlockUpdatedRequired) 1 else 0)
        builder.putBits(5, y)
        builder.putBits(14, type)
        builder.putBits(5, x)
    }
}
