package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.net.game.*


internal val ItemEncoder = MessageEncoder(GroundItemMessage::class) { alloc, message ->
    when (message.type) {
        GroundItemMessage.Type.CREATE -> {
            val builder = GameFrameBuilder(alloc, 33)
            builder.put(DataType.SHORT, DataOrder.LITTLE, message.id)
            builder.put(DataType.BYTE, message.position.blockHash())
            builder.put(DataType.SHORT, DataTransformation.ADD, message.amount)
            return@MessageEncoder builder.toGameFrame()
        }

        GroundItemMessage.Type.UPDATE -> {
            val builder = GameFrameBuilder(alloc, 14)
            builder.put(DataType.BYTE, message.position.blockHash())
            builder.put(DataType.SHORT, message.id)
            builder.put(DataType.SHORT, message.previousAmount)
            builder.put(DataType.SHORT, message.amount)
            return@MessageEncoder builder.toGameFrame()
        }

        GroundItemMessage.Type.REMOVE -> {
            val builder = GameFrameBuilder(alloc, 240)
            builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.position.blockHash())
            builder.put(DataType.SHORT, message.id)
            return@MessageEncoder builder.toGameFrame()
        }
    }
}
private val ItemDropDecoder = MessageDecoder(135) { frame ->
    val reader = GameFrameReader(frame)
    val id: Int = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    val slot: Int = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    return@MessageDecoder ItemDropMessage(id, slot)
}
private val ItemOption1 = MessageDecoder(66) { frame ->
    val reader = GameFrameReader(frame)
    val x = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val id = reader.getSigned(DataType.SHORT).toInt()
    val y = reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder GroundItemOptionMessage(id, Position(x, y), 1)//option 3
}
private val ItemOption2 = MessageDecoder(33) { frame ->
    val reader = GameFrameReader(frame)
    val id: Int = reader.getSigned(DataType.SHORT).toInt()
    val x: Int = reader.getSigned(DataType.SHORT).toInt()
    val y: Int = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    return@MessageDecoder GroundItemOptionMessage(id, Position(x, y), 2)
}
private val ItemOnGroundItemDecoder = MessageDecoder(101) { frame ->
    val reader = GameFrameReader(frame)
    val x = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val slot = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val itemId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val groundItemId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val y = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val widgetHash = reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE).toInt()
    return@MessageDecoder ItemOnGroundItemMessage(x, y, slot, itemId, groundItemId, widgetHash)
}
private val ItemOnItemDecoder = MessageDecoder(27) { frame ->
    val reader = GameFrameReader(frame)
    val usedSlot = reader.getUnsigned(DataType.SHORT).toInt()
    val withHash = reader.getUnsigned(DataType.INT, DataOrder.LITTLE).toInt()
    val withSlot = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val usedHash = reader.getUnsigned(DataType.INT, DataOrder.LITTLE).toInt()
    val usedId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    val withId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder ItemOnItemMessage(usedId, usedSlot, usedHash, withId, withSlot, withHash)
}
private val ItemOnNPCDecoder = MessageDecoder(134) { frame ->
    val reader = GameFrameReader(frame)
    val widgetHash = reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE).toInt()
    val slot = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val npcIndex = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val itemId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder ItemOnNPCMessage(itemId, slot, npcIndex, widgetHash)
}
private val ItemOnObjectDecoder = MessageDecoder(134) { frame ->
    val reader = GameFrameReader(frame)
    val x = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    val itemId = reader.getUnsigned(DataType.SHORT).toInt()
    val y = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val slot = reader.getUnsigned(DataType.SHORT).toInt()
    val widgetHash = reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE).toInt()
    val objectId = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    return@MessageDecoder ItemOnObjectMessage(itemId, slot, widgetHash, objectId, Position(x, y))
}
private val MagicOnItemDecoder = MessageDecoder(253) { frame ->
    val reader = GameFrameReader(frame)
    val spellId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val tabId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    val slot = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    reader.getUnsigned(DataType.INT, DataOrder.LITTLE)
    val itemId = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD)
    return@MessageDecoder MagicOnItemMessage(tabId, spellId, slot, itemId)
}

internal val ItemDecoders = arrayOf(
    ItemDropDecoder, ItemOption1, ItemOption2,
    ItemOnGroundItemDecoder, ItemOnItemDecoder,
    ItemOnNPCDecoder, ItemOnObjectDecoder, MagicOnItemDecoder
)