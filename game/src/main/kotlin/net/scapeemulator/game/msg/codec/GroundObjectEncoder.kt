package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.msg.GroundObjectMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val GroundObjectEncoder = MessageEncoder(GroundObjectMessage::class) { alloc, message ->
    fun Position.positionHash() = (getChunkOffsetX() shl 4) or (getChunkOffsetY() and 0x7)
    when (message.type) {
        GroundObjectMessage.Type.ANIMATE -> {
            val builder = GameFrameBuilder(alloc, 20)
            builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.position.blockHash())
            builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.hash())
            builder.put(DataType.SHORT, DataOrder.LITTLE, message.animation)
            return@MessageEncoder builder.toGameFrame()
        }

        GroundObjectMessage.Type.REMOVE -> {
            val builder = GameFrameBuilder(alloc, 195)
            builder.put(DataType.BYTE, DataTransformation.NEGATE, message.hash())
            builder.put(DataType.BYTE, message.position.positionHash())
            return@MessageEncoder builder.toGameFrame()
        }

        GroundObjectMessage.Type.CREATE -> {
            val builder = GameFrameBuilder(alloc, 179)
            builder.put(DataType.BYTE, DataTransformation.ADD, message.hash())
            builder.put(DataType.BYTE, message.position.positionHash())
            builder.put(DataType.SHORT, DataTransformation.ADD, message.id)
            return@MessageEncoder builder.toGameFrame()
        }
    }
}