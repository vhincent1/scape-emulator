package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.DisplayModelMessage
import net.scapeemulator.game.msg.DisplayModelMessage.Type
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val displayModelEncoder = handleEncoder(DisplayModelMessage::class) { alloc, message ->
    var builder: GameFrameBuilder
    val value = message.interfaceId shl 16 or message.childId
    when (message.type) {
        Type.PLAYER -> {
            builder = GameFrameBuilder(alloc, 66)
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, 0) //packet count
            // putIntA
            builder.put(DataType.INT, DataOrder.MIDDLE, value)
            return@handleEncoder builder.toGameFrame()
        }

        Type.NPC -> {
            builder = GameFrameBuilder(alloc, 73)
            builder.put(DataType.SHORT, DataTransformation.ADD, message.nodeId)
            builder.put(DataType.INT, DataOrder.LITTLE, value)
            return@handleEncoder builder.toGameFrame()
        }

        Type.ITEM -> {
            val value = if (message.amount > 0) message.amount else message.zoom
            builder = GameFrameBuilder(alloc, 50)
            builder.put(DataType.INT, value)
            builder.put(DataType.INT, DataOrder.BIG, value)
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.nodeId)
            builder.put(DataType.SHORT, DataOrder.LITTLE, 0) //packet count
            return@handleEncoder builder.toGameFrame()
        }

        Type.MODEL -> {
            builder = GameFrameBuilder(alloc, 67)
            builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.nodeId)
            builder.put(DataType.INT, DataOrder.LITTLE, value)
            return@handleEncoder builder.toGameFrame()
        }
    }
    return@handleEncoder builder.toGameFrame()
}