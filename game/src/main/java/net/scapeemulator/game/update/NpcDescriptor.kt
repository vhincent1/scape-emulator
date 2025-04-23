package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

abstract class NpcDescriptor(npc: Npc) {
    private val blocks: MutableMap<Class<out NpcBlock?>?, NpcBlock> = HashMap<Class<out NpcBlock?>?, NpcBlock>()

    init {
        if (npc.isAnimationUpdated) addBlock(AnimationNpcBlock(npc))

        if (npc.isSpotAnimationUpdated) addBlock(SpotAnimationNpcBlock(npc))
    }

    private fun addBlock(block: NpcBlock) {
        blocks.put(block.javaClass, block)
    }

    val isBlockUpdatedRequired: Boolean
        get() = !blocks.isEmpty()

    fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder, blockBuilder: GameFrameBuilder) {
        encodeDescriptor(message, builder, blockBuilder)

        if (this.isBlockUpdatedRequired) {
            var flags = 0
            for (block in blocks.values) flags = flags or block.flag

            if (flags > 0xFF) {
                flags = flags or 0x8
                blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, flags)
            } else {
                blockBuilder.put(DataType.BYTE, flags)
            }

            encodeBlock(message, blockBuilder, AnimationNpcBlock::class.java)
            encodeBlock(message, blockBuilder, SpotAnimationNpcBlock::class.java)
        }
    }

    private fun encodeBlock(message: NpcUpdateMessage, builder: GameFrameBuilder, type: Class<out NpcBlock?>?) {
        val block = blocks.get(type)
        if (block != null) block.encode(message, builder)
    }

    abstract fun encodeDescriptor(
        message: NpcUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    )

    companion object {
        @JvmStatic
        fun create(npc: Npc): NpcDescriptor {
            val firstDirection = npc.firstDirection
            val secondDirection = npc.secondDirection

            if (firstDirection == Direction.NONE) return IdleNpcDescriptor(npc)
            else if (secondDirection == Direction.NONE) return WalkNpcDescriptor(npc)
            else return RunNpcDescriptor(npc)
        }
    }
}
