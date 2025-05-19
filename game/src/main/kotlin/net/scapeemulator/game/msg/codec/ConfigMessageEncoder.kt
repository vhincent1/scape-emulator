package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ConfigMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val ConfigMessageEncoder = handleEncoder(ConfigMessage::class) { alloc, message ->
    val id = message.id
    val value = message.value

    if (value >= -128 && value <= 127) {
        val builder = GameFrameBuilder(alloc, 60)
        builder.put(DataType.SHORT, DataTransformation.ADD, id)
        builder.put(DataType.BYTE, DataTransformation.NEGATE, value)
        return@handleEncoder builder.toGameFrame()
    } else {
        val builder = GameFrameBuilder(alloc, 226)
        builder.put(DataType.INT, value)
        builder.put(DataType.SHORT, DataTransformation.ADD, id)
        return@handleEncoder builder.toGameFrame()
    }
}