package net.scapeemulator.game.update

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.SpotAnimation
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

class SpotAnimationPlayerBlock(player: Player) : PlayerBlock(0x100) {
    private val spotAnimation: SpotAnimation? = player.spotAnimation

    override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.SHORT, DataOrder.LITTLE, spotAnimation!!.id)
        builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (spotAnimation.height shl 16) or spotAnimation.delay)
    }
}