package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.ScriptMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder
import java.io.IOException

class ScriptMessageEncoder : MessageEncoder<ScriptMessage>(ScriptMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: ScriptMessage): GameFrame {
        val id = message.id
        val types = message.types
        val parameters = message.parameters

        val builder = GameFrameBuilder(alloc, 115, GameFrame.Type.VARIABLE_SHORT)
        builder.put(DataType.SHORT, 0)
        builder.putString(types)

        for (i in types.length - 1 downTo 0) {
            if (types[i] == 's') {
                builder.putString(parameters[i] as String)
            } else {
                builder.put(DataType.INT, (parameters[i] as Number).toInt())
            }
        }

        builder.put(DataType.INT, id)
        return builder.toGameFrame()
    }
}
