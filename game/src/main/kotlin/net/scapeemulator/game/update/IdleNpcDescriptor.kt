package net.scapeemulator.game.update

import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class IdleNpcDescriptor(npc: Npc) : NpcDescriptor(npc) {
    public override fun encodeDescriptor(
        message: NpcUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        if (isBlockUpdatedRequired) {
            builder.putBits(1, 1)
            builder.putBits(2, 0)
        } else {
            builder.putBits(1, 0)
        }
    }
}
