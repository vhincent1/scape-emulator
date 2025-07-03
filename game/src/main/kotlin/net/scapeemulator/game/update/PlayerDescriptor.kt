package net.scapeemulator.game.update

import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder
import kotlin.reflect.KClass

abstract class PlayerDescriptor(player: Player, tickets: IntArray) {
    private val blocks: MutableMap<KClass<out PlayerBlock>, PlayerBlock> = HashMap()

    init {
        if (player.isActive) {
            /*
             * This active check is required for the RemovePlayerDescriptor.
             * The player id would be -1 in this case, which causes the
             * following code to crash. Skipping this code doesn't matter as no
             * update blocks can be sent when removing a player.
             */
            val id = player.index - 1
            val ticket = player.appearanceTicket
            if (tickets[id] != ticket) {
                tickets[id] = ticket
                addBlock(AppearancePlayerBlock(player))
            }
        }
        if (player.isChatUpdated) addBlock(ChatPlayerBlock(player))
        if (player.isAnimationUpdated) addBlock(AnimationPlayerBlock(player))
        if (player.isSpotAnimationUpdated) addBlock(SpotAnimationPlayerBlock(player))
        if (player.isHitUpdated) addBlock(PlayerHitBlock(player))
        if (player.isHit2Updated) addBlock(PlayerHitBlock2(player))
        if (player.isFacingUpdated) addBlock(PlayerFaceBlock(player))
        if (player.isForceChatUpdated) addBlock(ForceChatBlock(player))
        if (player.isFacePositionUpdated) addBlock(PlayerFacePositionBlock(player))
    }

    private fun addBlock(block: PlayerBlock) = blocks.put(block::class, block)
    val isBlockUpdatedRequired: Boolean get() = blocks.isNotEmpty()

    fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder, blockBuilder: GameFrameBuilder) {
        encodeDescriptor(message, builder, blockBuilder)
        if (this.isBlockUpdatedRequired) {
            var flags = 0
            for (block in blocks.values) flags = flags or block.flag
            if (flags > 0xFF) {
                flags = flags or 0x10
                blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, flags)
            } else {
                blockBuilder.put(DataType.BYTE, flags)
            }
            //ordering matter
            /*
        if (flags.isChatTextUpdateRequired()) {
			mask |= 0x80;
		}
		if(flags.isHitUpdateRequired()) {
			mask |= 0x1;
		}
		if(flags.isAnimationUpdateRequired()) {
			mask |= 0x8;
		}
		if (flags.isAppearanceUpdateRequired() || forceAppearance) {
			mask |= 0x4;
		}
		if (flags.isEntityFocusUpdateRequired()) {
			mask |= 0x2;
		}
		if (flags.isForceMovementRequired()) {
			mask |= 0x400;
		}
		if (flags.isForceTextUpdateRequired()) {
			mask |= 0x20;
		}
		if(flags.isHit2UpdateRequired()) {
			mask |= 0x200;
		}
		if(flags.isForceMovementRequired()) {
			//mask |= 0x800;
		}
		if (flags.isGraphicsUpdateRequired()) {
			mask |= 0x100;
		}
		if (flags.isFaceLocationUpdateRequired()) {
			mask |= 0x40;
		}	*/
            encodeBlock(message, blockBuilder, ChatPlayerBlock::class)
            encodeBlock(message, blockBuilder, PlayerHitBlock::class)
            encodeBlock(message, blockBuilder, AnimationPlayerBlock::class)
            encodeBlock(message, blockBuilder, AppearancePlayerBlock::class)
            encodeBlock(message, blockBuilder, PlayerFaceBlock::class)
            encodeBlock(message, blockBuilder, ForceChatBlock::class)
            encodeBlock(message, blockBuilder, PlayerHitBlock2::class)
            encodeBlock(message, blockBuilder, SpotAnimationPlayerBlock::class)
            encodeBlock(message, blockBuilder, PlayerFacePositionBlock::class)
        }
    }

    private fun encodeBlock(message: PlayerUpdateMessage, builder: GameFrameBuilder, type: KClass<out PlayerBlock>) {
        val block = blocks[type]
        block?.encode(message, builder)
    }

    abstract fun encodeDescriptor(
        message: PlayerUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    )

    companion object {
        @JvmStatic
        fun create(player: Player, tickets: IntArray): PlayerDescriptor {
            val firstDirection = player.firstDirection
            val secondDirection = player.secondDirection
            return if (firstDirection == Direction.NONE) IdlePlayerDescriptor(player, tickets)
            else if (secondDirection == Direction.NONE) WalkPlayerDescriptor(player, tickets)
            else RunPlayerDescriptor(player, tickets)
        }
    }
}
