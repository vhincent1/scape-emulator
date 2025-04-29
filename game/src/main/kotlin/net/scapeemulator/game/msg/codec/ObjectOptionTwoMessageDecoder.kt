package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ObjectOptionTwoMessage
import net.scapeemulator.game.net.game.*

class ObjectOptionTwoMessageDecoder : MessageDecoder<ObjectOptionTwoMessage>(194) {
    override fun decode(frame: GameFrame): ObjectOptionTwoMessage {
        val reader = GameFrameReader(frame)
        val y = reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
        val x = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
        val id = reader.getSigned(DataType.SHORT).toInt()
        return ObjectOptionTwoMessage(x, y, id)
    }
}
