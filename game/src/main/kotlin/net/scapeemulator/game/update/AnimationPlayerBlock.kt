package net.scapeemulator.game.update

import net.scapeemulator.game.model.Animation
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

class AnimationPlayerBlock(player: Player) : PlayerBlock(0x8) {
    private val animation: Animation?

    init {
        this.animation = player.animation
    }

    public override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.SHORT, animation!!.id)
        builder.put(DataType.BYTE, animation.delay)
    }
}
