package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ScriptIntMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val scriptIntMessageEncoder = handleEncoder(ScriptIntMessage::class) { alloc, message ->
    val id = message.id
    val value = message.value

    if (value >= -128 && value <= 127) {
        val builder = GameFrameBuilder(alloc, 65)
        builder.put(DataType.SHORT, DataOrder.LITTLE, 0)
        builder.put(DataType.BYTE, DataTransformation.NEGATE, value)
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, id)
        return@handleEncoder builder.toGameFrame()
    } else {
        val builder = GameFrameBuilder(alloc, 69)
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, 0)
        builder.put(DataType.INT, value)
        builder.put(DataType.SHORT, DataTransformation.ADD, id)
        return@handleEncoder builder.toGameFrame()
    }
}