package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class WalkNpcDescriptor(npc: Npc) : NpcDescriptor(npc) {
    private val direction: Direction

    init {
        this.direction = npc.firstDirection
    }

    public override fun encodeDescriptor(
        message: NpcUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        builder.putBits(1, 1)
        builder.putBits(2, 1)
        builder.putBits(3, direction.toInteger())
        builder.putBits(1, if (isBlockUpdatedRequired) 1 else 0)
    }
}
