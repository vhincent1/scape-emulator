package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.InterfaceSlottedItemsMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder

class InterfaceSlottedItemsMessageEncoder :
    MessageEncoder<InterfaceSlottedItemsMessage>(InterfaceSlottedItemsMessage::class.java) {
    override fun encode(alloc: ByteBufAllocator, message: InterfaceSlottedItemsMessage): GameFrame {
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

        return builder.toGameFrame()
    }
}
