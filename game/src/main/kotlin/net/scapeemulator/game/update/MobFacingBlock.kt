package net.scapeemulator.game.update

import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

class PlayerFaceBlock(player: Player) : PlayerBlock(0x2) {
    private val index: Int = if (player.faceMob == null) -1 else player.faceMob?.getClientIndex()!!
    override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.SHORT, DataTransformation.ADD, index)
    }
}

class PlayerFacePositionBlock(player: Player) : PlayerBlock(0x40) {
    private val position: Position? = player.facePosition
    override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        // The measure is in half tiles, you must add a half tile because the player is located on the middle of a tile
        builder.put(DataType.SHORT, position?.x!! * 2 + 1);
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.y * 2 + 1);
    }
}


class NpcFaceBlock(npc: Npc) : NpcBlock(0x4) {
    private val index: Int = if (npc.faceMob == null) -1 else npc.faceMob?.getClientIndex()!!
    override fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.SHORT, DataTransformation.ADD, index)
    }
}

class NpcFacePositionBlock(npc: Npc) : NpcBlock(0x200) {
    private val position: Position? = npc.facePosition
    override fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder) {
        // The measure is in half tiles, you must add a half tile because the player is located on the middle of a tile
        builder.put(DataType.SHORT, DataTransformation.ADD, position?.x!! * 2 + 1)
        builder.put(DataType.SHORT, position.y * 2 + 1)
    }
}








