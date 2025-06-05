package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.*
import net.scapeemulator.game.net.game.*

internal val ServerMessageEncoder = MessageEncoder(ServerMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 70, GameFrame.Type.VARIABLE_BYTE)
    builder.putString(message.text)
    return@MessageEncoder builder.toGameFrame()
}

internal val SystemUpdateEncoder = MessageEncoder(SystemUpdateMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 85)
    builder.put(DataType.SHORT, message.time)
    return@MessageEncoder builder.toGameFrame()
}

internal val MinimapStatusEncoder = MessageEncoder(MiniMapStatusMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 192)
    builder.put(DataType.BYTE, message.setting)
    return@MessageEncoder builder.toGameFrame()
}

internal val HintArrowEncoder = MessageEncoder(HintIconMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 217)
    builder.put(DataType.BYTE, message.slot shl 6 or message.targetType) //10 player 1 npc 2 loc
    builder.put(DataType.BYTE, if (message.arrowId < 32768) 0 else 1) // 0 full 1 hollow
    if (message.arrowId > 0) {
        if (message.remove) {
            builder.put(DataType.SHORT, 0)
            builder.put(DataType.SHORT, 0)
            builder.put(DataType.BYTE, 0)
        } else if (message.targetType == 1 || message.targetType == 10) {
            builder.put(DataType.SHORT, message.targetId)
            builder.put(DataType.SHORT, 0)
            builder.put(DataType.BYTE, 0)
        } else if (message.entity?.position != null || message.position != null) {
            val pos = message.entity?.position ?: message.position!!
            builder.put(DataType.SHORT, pos.x)
            builder.put(DataType.SHORT, pos.y)
            builder.put(DataType.BYTE, pos.height)
        }
        builder.put(DataType.SHORT, message.modelId)//model id
    }
    return@MessageEncoder builder.toGameFrame()
}

internal val EnergyMessageEncoder = MessageEncoder(EnergyMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 234)
    builder.put(DataType.BYTE, message.energy)
    return@MessageEncoder builder.toGameFrame()
}

internal val InteractionOptionEncoder = MessageEncoder(InteractionOptionMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 44, GameFrame.Type.VARIABLE_BYTE)
    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, -1)
    builder.put(DataType.BYTE, if (message.position == 0) 1 else 0)
    builder.put(DataType.BYTE, message.position + 1)
    builder.putString(message.name)
    return@MessageEncoder builder.toGameFrame()
}

//TODO doesn't work
internal val WeightEncoder = MessageEncoder(WeightMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 174)
    builder.put(DataType.SHORT, 0.0)
    return@MessageEncoder builder.toGameFrame()
}

internal val AccessMaskEncoder = MessageEncoder(AccessMaskMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 165)
    builder.put(DataType.SHORT, DataOrder.LITTLE, 0)//packet count
    builder.put(DataType.SHORT, DataOrder.LITTLE, message.length)
    builder.put(DataType.INT, message.interfaceId shl 16 or message.child)
    builder.put(DataType.SHORT, DataTransformation.ADD, message.offset)
    builder.put(DataType.INT, DataOrder.MIDDLE, message.id)
    return@MessageEncoder builder.toGameFrame()
}

internal val PlacementCoordsEncoder = MessageEncoder(PlacementCoordsMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 26)
    builder.put(DataType.BYTE, DataTransformation.NEGATE, message.x)
    builder.put(DataType.BYTE, message.y)
    return@MessageEncoder builder.toGameFrame()
}