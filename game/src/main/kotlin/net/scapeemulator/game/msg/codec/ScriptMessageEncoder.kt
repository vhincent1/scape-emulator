package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ScriptMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val ScriptMessageEncoder = handleEncoder(ScriptMessage::class) { alloc, message ->
    val id = message.id
    val types = message.types
    val parameters = message.parameters
    val builder = GameFrameBuilder(alloc, 115, GameFrame.Type.VARIABLE_SHORT)
    builder.put(DataType.SHORT, 0) // packet counter
    builder.putString(types)
    for (i in (types.length - 1) downTo 0) {
        if (types[i] == 's') {
            builder.putString(parameters[i] as String)
        } else {
            builder.put(DataType.INT, (parameters[i] as Number).toInt())
        }
    }
    builder.put(DataType.INT, id)
    return@handleEncoder builder.toGameFrame()
}