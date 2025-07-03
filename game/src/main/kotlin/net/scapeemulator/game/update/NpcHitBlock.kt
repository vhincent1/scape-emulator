package net.scapeemulator.game.update

import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.model.Skill
import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

class NpcHitBlock(val npc: Npc) : NpcBlock(0x40) {
    override fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder) {
        val hit = npc.primaryHit ?: return
        val hp = npc.skillSet.getCurrentLevel(Skill.HITPOINTS)
        val maxHp = npc.skillSet.getMaximumLevel(Skill.HITPOINTS)
        var ratio = (hp * 255) / maxHp
        if (hp > maxHp) ratio = maxHp / maxHp
        builder.put(DataType.BYTE, hit.damage)
        builder.put(DataType.BYTE, DataTransformation.NEGATE, hit.type.ordinal)
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, ratio)
    }
}

class NpcHitBlock2(val npc: Npc) : NpcBlock(0x2) {
    override fun encode(message: NpcUpdateMessage, builder: GameFrameBuilder) {
        builder.put(DataType.BYTE, DataTransformation.NEGATE, npc.secondaryHit?.damage!!)
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, npc.secondaryHit?.type!!.ordinal)
    }
}