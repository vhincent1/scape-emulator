package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class RunNpcDescriptor(npc: Npc) : NpcDescriptor(npc) {
    private val firstDirection: Direction
    private val secondDirection: Direction

    init {
        this.firstDirection = npc.firstDirection
        this.secondDirection = npc.secondDirection
    }

    public override fun encodeDescriptor(
        message: NpcUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        builder.putBits(1, 1)
        builder.putBits(2, 2)
        builder.putBits(1, 1)
        builder.putBits(3, firstDirection.toInteger())
        builder.putBits(3, secondDirection.toInteger())
        builder.putBits(1, if (isBlockUpdatedRequired) 1 else 0)
    }
}
