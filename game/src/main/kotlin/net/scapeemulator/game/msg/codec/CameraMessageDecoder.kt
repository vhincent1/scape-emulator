package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.CameraMessage
import net.scapeemulator.game.net.game.*
import java.io.IOException

class CameraMessageDecoder : MessageDecoder<CameraMessage>(21) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): CameraMessage {
        val reader = GameFrameReader(frame)
        val pitch = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
        val yaw = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
        return CameraMessage(yaw, pitch)
    }
}
