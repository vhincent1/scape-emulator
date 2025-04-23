package net.scapeemulator.game.update

import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.model.SpotAnimation
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

class SpotAnimationNpcBlock(npc: Npc) : NpcBlock(0x80) {
    private val spotAnimation: SpotAnimation?//todo check

    init {
        this.spotAnimation = npc.spotAnimation
    }

    override fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.SHORT, DataTransformation.ADD, spotAnimation!!.id)
        builder.put(DataType.INT, DataOrder.LITTLE, (spotAnimation.height shl 16) or spotAnimation.delay)
    }
}
