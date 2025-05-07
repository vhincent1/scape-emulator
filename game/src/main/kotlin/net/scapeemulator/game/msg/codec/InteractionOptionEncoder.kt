package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.InteractionOptionMessage
import net.scapeemulator.game.net.game.*

internal val interactionOptionEncoder = handleEncoder(InteractionOptionMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 44, GameFrame.Type.VARIABLE_BYTE)
    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, -1)
    builder.put(DataType.BYTE, if (message.index == 0) 1 else 0)
    builder.put(DataType.BYTE, message.index + 1)
    builder.putString(message.name)
    return@handleEncoder builder.toGameFrame()
}