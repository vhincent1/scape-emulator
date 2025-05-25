package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder
import kotlin.reflect.KClass

abstract class NpcDescriptor(npc: Npc) {
    private val blocks: MutableMap<KClass<out NpcBlock>, NpcBlock> = HashMap()

    init {
        /*if(npc.getUpdateFlags().isHitUpdateRequired()) {
			mask |= 0x40;
		}
		if(npc.getUpdateFlags().isHit2UpdateRequired()) {
			mask |= 0x2;
		}
		if(npc.getUpdateFlags().isAnimationUpdateRequired()) {
			mask |= 0x10;
		}
		if(npc.getUpdateFlags().isEntityFocusUpdateRequired()) {
			mask |= 0x4;
		}
		if(npc.getUpdateFlags().isGraphicsUpdateRequired()) {
			mask |= 0x80;
		}
		//0x1
		if(npc.getUpdateFlags().isForceTextUpdateRequired()) {
			mask |= 0x20;
		}
		//0x100
		if(npc.getUpdateFlags().isFaceLocationUpdateRequired()) {
			mask |= 0x200;
		}*/
        if (npc.isHitUpdated) addBlock(NpcHitBlock(npc))
        if (npc.isHit2Updated) addBlock(NpcHitBlock2(npc))
        if (npc.isAnimationUpdated) addBlock(AnimationNpcBlock(npc))
        if (npc.isSpotAnimationUpdated) addBlock(SpotAnimationNpcBlock(npc))
    }

    private fun addBlock(block: NpcBlock) {
        blocks.put(block::class, block)
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
            encodeBlock(message, blockBuilder, NpcHitBlock::class)
            encodeBlock(message, blockBuilder, NpcHitBlock2::class)
            encodeBlock(message, blockBuilder, AnimationNpcBlock::class)
            encodeBlock(message, blockBuilder, SpotAnimationNpcBlock::class)
        }
    }

    private fun encodeBlock(message: NpcUpdateMessage, builder: GameFrameBuilder, type: KClass<out NpcBlock>?) {
        val block = blocks[type]
        block?.encode(message, builder)
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

            return if (firstDirection == Direction.NONE) IdleNpcDescriptor(npc)
            else if (secondDirection == Direction.NONE) WalkNpcDescriptor(npc)
            else RunNpcDescriptor(npc)
        }
    }
}
