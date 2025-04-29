package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.EquipItemMessage
import net.scapeemulator.game.net.game.*
import java.io.IOException

class EquipItemMessageDecoder : MessageDecoder<EquipItemMessage>(55) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): EquipItemMessage {
        val reader = GameFrameReader(frame)
        val itemId = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
        val itemSlot = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
        val inter = reader.getSigned(DataType.INT, DataOrder.MIDDLE).toInt()
        val id = (inter shr 16) and 0xFFFF
        val slot = inter and 0xFFFF
        return EquipItemMessage(id, slot, itemSlot, itemId)
    }
}
