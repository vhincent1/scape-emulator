package net.scapeemulator.game.update

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

class ForceChatBlock(val player: Player) : PlayerBlock(0x20) {
    override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        builder.putString(player.forceChat)
    }
}