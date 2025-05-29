package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ResetMinimapFlagMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

//class ResetMinimapFlagMessageEncoder : MessageEncoder<ResetMinimapFlagMessage>(ResetMinimapFlagMessage::class) {
//    @Throws(IOException::class)
//    override fun encode(alloc: ByteBufAllocator, message: ResetMinimapFlagMessage): GameFrame {
//        val builder = GameFrameBuilder(alloc, 153)
//        return builder.toGameFrame()
//    }
//}

internal val ResetMinimapFlagMessageEncoder = handleEncoder(ResetMinimapFlagMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 153)
    //  int minimapFlagId = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT); //todo check
    return@handleEncoder builder.toGameFrame()
}