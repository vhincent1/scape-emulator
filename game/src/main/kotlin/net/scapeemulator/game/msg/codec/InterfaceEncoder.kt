package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.*
import net.scapeemulator.game.net.game.*

@JvmField
internal val InterfaceRootEncoder = handleEncoder(InterfaceRootMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 145, GameFrame.Type.FIXED)
    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.id)
    builder.put(DataType.BYTE, DataTransformation.ADD, message.type)//type
    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, 0)//packetcount
    return@handleEncoder builder.toGameFrame()
}

internal val InterfaceOpenEncoder = handleEncoder(InterfaceOpenMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 155)
    builder.put(DataType.BYTE, message.type) // isWalkable ? 1 : 0
    builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
    builder.put(DataType.SHORT, DataTransformation.ADD, 0)
    builder.put(DataType.SHORT, message.childId)
    return@handleEncoder builder.toGameFrame()
}

internal val InterfaceCloseEncoder = handleEncoder(InterfaceCloseMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 149)
    builder.put(DataType.SHORT, 0)
//    builder.put(DataType.SHORT, message.id)
//    builder.put(DataType.SHORT, message.slot)
    //TODO check
    // builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
    builder.put(DataType.INT, (message.id shl 16) or message.slot)

    return@handleEncoder builder.toGameFrame()
}

internal val InterfaceVisibleEncoder = handleEncoder(InterfaceVisibleMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 21)
    builder.put(DataType.BYTE, DataTransformation.NEGATE, if (message.isVisible) 0 else 1)
    builder.put(DataType.SHORT, 0)
    builder.put(DataType.INT, DataOrder.LITTLE, (message.id shl 16) or message.slot)
    return@handleEncoder builder.toGameFrame()
}

internal val InterfaceTextEncoder = handleEncoder(InterfaceTextMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 171, GameFrame.Type.VARIABLE_SHORT)
    builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
    builder.putString(message.text)
    builder.put(DataType.SHORT, DataTransformation.ADD, 0)
    return@handleEncoder builder.toGameFrame()
}

internal val InterfaceItemsMessageEncoder = handleEncoder(InterfaceItemsMessage::class) { alloc, message ->
    val items = message.items
    val builder = GameFrameBuilder(alloc, 105, GameFrame.Type.VARIABLE_SHORT)
    builder.put(DataType.INT, (message.id shl 16) or message.slot)
    builder.put(DataType.SHORT, message.type)
    builder.put(DataType.SHORT, items.size)

    for (item in items) {
        if (item == null) {
            builder.put(DataType.BYTE, DataTransformation.SUBTRACT, 0)
            builder.put(DataType.SHORT, 0)
        } else {
            val amount = item.amount
            if (amount >= 255) {
                builder.put(DataType.BYTE, DataTransformation.SUBTRACT, 255)
                builder.put(DataType.INT, amount)
            } else {
                builder.put(DataType.BYTE, DataTransformation.SUBTRACT, amount)
            }
            builder.put(DataType.SHORT, item.id + 1)
        }
    }
    return@handleEncoder builder.toGameFrame()
}

internal val InterfaceSlottedItemsEncoder = handleEncoder(InterfaceSlottedItemsMessage::class) { alloc, message ->
    val items = message.items
    val builder = GameFrameBuilder(alloc, 22, GameFrame.Type.VARIABLE_SHORT)
    builder.put(DataType.INT, (message.id shl 16) or message.slot)
    builder.put(DataType.SHORT, message.type)

    for (slottedItem in items) {
        val slot = slottedItem.slot
        builder.putSmart(slot)

        val item = slottedItem.item
        if (item == null) {
            builder.put(DataType.SHORT, 0)
        } else {
            val amount = item.amount
            builder.put(DataType.SHORT, item.id + 1)
            if (amount >= 255) {
                builder.put(DataType.BYTE, 255)
                builder.put(DataType.INT, amount)
            } else {
                builder.put(DataType.BYTE, amount)
            }
        }
    }
    return@handleEncoder builder.toGameFrame()
}

internal val InterfaceResetItemsEncoder = handleEncoder(InterfaceResetItemsMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 144)
    builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.id shl 16) or message.slot)
    return@handleEncoder builder.toGameFrame()
}

internal val AnimateInterfaceEncoder = handleEncoder(AnimateInterfaceMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 36)
    builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, (message.interfaceId shl 16) + message.childId)
    builder.put(DataType.SHORT, DataOrder.LITTLE, message.animationId)
    builder.put(DataType.SHORT, DataTransformation.ADD, 0)//packet count
    return@handleEncoder builder.toGameFrame()
}