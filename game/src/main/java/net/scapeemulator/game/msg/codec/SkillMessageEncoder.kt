package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.SkillMessage
import net.scapeemulator.game.net.game.*

class SkillMessageEncoder : MessageEncoder<SkillMessage>(SkillMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: SkillMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 38)
        builder.put(DataType.BYTE, DataTransformation.ADD, message.level)
        builder.put(DataType.INT, DataOrder.MIDDLE, message.experience)
        builder.put(DataType.BYTE, message.skill)
        return builder.toGameFrame()
    }
}
