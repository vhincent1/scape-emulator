package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ServerMessage
import net.scapeemulator.game.msg.SystemUpdateMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val serverMessageEncoder = handleEncoder(ServerMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 70, GameFrame.Type.VARIABLE_BYTE)
    builder.putString(message.text)
    return@handleEncoder builder.toGameFrame()
}

internal val systemUpdateEncoder = handleEncoder(SystemUpdateMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 85)
    builder.put(DataType.SHORT, message.time)
    return@handleEncoder builder.toGameFrame()
}