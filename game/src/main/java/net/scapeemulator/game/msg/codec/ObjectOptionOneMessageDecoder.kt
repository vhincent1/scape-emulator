package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ObjectOptionOneMessage
import net.scapeemulator.game.net.game.*

class ObjectOptionOneMessageDecoder : MessageDecoder<ObjectOptionOneMessage>(254) {
    override fun decode(frame: GameFrame): ObjectOptionOneMessage {
        val reader = GameFrameReader(frame)
        val x = reader.getSigned(DataType.SHORT, DataOrder.LITTLE).toInt()
        val id = reader.getSigned(DataType.SHORT, DataTransformation.ADD).toInt()
        val y = reader.getSigned(DataType.SHORT).toInt()
        return ObjectOptionOneMessage(x, y, id)
    }
}
