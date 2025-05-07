package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.CameraMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

internal val cameraMessageDecoder = handleDecoder(21) { frame ->
    val reader = GameFrameReader(frame)
    val pitch = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    val yaw = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    return@handleDecoder CameraMessage(yaw, pitch)
}