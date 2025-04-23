package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceItemsMessage
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder

class InterfaceItemsMessageEncoder : MessageEncoder<InterfaceItemsMessage>(InterfaceItemsMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: InterfaceItemsMessage): GameFrame {
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

        return builder.toGameFrame()
    }
}
