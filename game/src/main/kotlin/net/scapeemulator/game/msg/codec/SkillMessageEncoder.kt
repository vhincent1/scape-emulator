package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.SkillMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

//class SkillMessageEncoder : MessageEncoder<SkillMessage>(SkillMessage::class) {
//    override fun encode(alloc: ByteBufAllocator, message: SkillMessage): GameFrame {
//        val builder = GameFrameBuilder(alloc, 38)
//        //putA ?
//        builder.put(DataType.BYTE, DataTransformation.ADD, message.level)
//        //putIntA
//        builder.put(DataType.INT, DataOrder.MIDDLE, message.experience)
//        builder.put(DataType.BYTE, message.skill)
//        return builder.toGameFrame()
//    }
//}

internal val skillMessageEncoder = handleEncoder(SkillMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 38)
    //putA ?
    builder.put(DataType.BYTE, DataTransformation.ADD, message.level)
    //putIntA
    builder.put(DataType.INT, DataOrder.MIDDLE, message.experience)
    builder.put(DataType.BYTE, message.skill)
    return@handleEncoder builder.toGameFrame()
}
