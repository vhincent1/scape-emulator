package net.scapeemulator.game.update

import net.scapeemulator.game.model.Animation
import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

class AnimationNpcBlock(npc: Npc) : NpcBlock(0x10) {
    private val animation: Animation?

    init {
        this.animation = npc.animation
    }

    override fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.SHORT, animation!!.id)
        builder.put(DataType.BYTE, animation.delay)
    }
}
