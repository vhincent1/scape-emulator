package net.scapeemulator.game.update

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class RemovePlayerDescriptor(player: Player, tickets: IntArray) : PlayerDescriptor(player, tickets) {
    public override fun encodeDescriptor(
        message: PlayerUpdateMessage,
        builder: GameFrameBuilder,
        blockBuilder: GameFrameBuilder
    ) {
        builder.putBits(1, 1)
        builder.putBits(2, 3)
    }
}
