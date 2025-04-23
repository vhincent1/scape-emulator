package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.EnergyMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder
import java.io.IOException

class EnergyMessageEncoder : MessageEncoder<EnergyMessage>(EnergyMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: EnergyMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 234)
        builder.put(DataType.BYTE, message.energy)
        return builder.toGameFrame()
    }
}
