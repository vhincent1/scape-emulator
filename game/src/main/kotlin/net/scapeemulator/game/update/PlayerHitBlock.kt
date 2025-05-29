package net.scapeemulator.game.update

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Skill
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

class PlayerHitBlock(val player: Player) : PlayerBlock(0x1) {
    override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        builder.putSmart(player.primaryHit?.damage!!)
        builder.put(DataType.BYTE, DataTransformation.ADD, player.primaryHit?.type!!.ordinal)//type
        val hp = player.skillSet.getCurrentLevel(Skill.HITPOINTS)
        val maxHp = player.skillSet.getMaximumLevel(Skill.HITPOINTS)
        var ratio = (hp * 255) / maxHp
        if (hp > maxHp) ratio = maxHp / maxHp // dead
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, ratio)
    }
}

class PlayerHitBlock2(val player: Player) : PlayerBlock(0x200) {
    override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.BYTE, player.secondaryHit?.damage!!)
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, player.secondaryHit?.type!!.ordinal)
    }
}
