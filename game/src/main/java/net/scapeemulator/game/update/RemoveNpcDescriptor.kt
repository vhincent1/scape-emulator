package net.scapeemulator.game.update

import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class RemoveNpcDescriptor(npc: Npc) : NpcDescriptor(npc) {
    public override fun encodeDescriptor(
        message: NpcUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        builder.putBits(1, 1)
        builder.putBits(2, 3)
    }
}
