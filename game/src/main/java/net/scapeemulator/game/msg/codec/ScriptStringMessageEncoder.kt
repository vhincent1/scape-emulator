package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.ScriptStringMessage
import net.scapeemulator.game.net.game.*
import java.io.IOException

class ScriptStringMessageEncoder : MessageEncoder<ScriptStringMessage>(ScriptStringMessage::class.java) {
    @Throws(IOException::class)
    override fun encode(alloc: ByteBufAllocator, message: ScriptStringMessage): GameFrame {
        val builder = GameFrameBuilder(alloc, 123)
        builder.put(DataType.SHORT, DataOrder.LITTLE, message.id)
        builder.put(DataType.SHORT, DataTransformation.ADD, 0)
        builder.putString(message.value)
        return builder.toGameFrame()
        /* TODO what is the difference between these two packets? they seem identical
		GameFrameBuilder builder = new GameFrameBuilder(alloc, 48);
		builder.put(DataType.SHORT, 0);
		builder.putString(message.getValue());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
		return builder.toGameFrame();
*/
    }
}
