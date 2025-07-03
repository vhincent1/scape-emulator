package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.msg.ExtendedButtonMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

//class ButtonMessageDecoder : MessageDecoder<ButtonMessage>(10) {
//    override fun decode(frame: GameFrame): ButtonMessage {
//        val reader = GameFrameReader(frame)
//        val button = reader.getSigned(DataType.INT).toInt()
//        val id = (button shr 16) and 0xFFFF
//        val slot = button and 0xFFFF
//        return ButtonMessage(id, slot)
//    }
//}

internal val ButtonMessageDecoder = MessageDecoder(10) { frame ->
    val reader = GameFrameReader(frame)
    val button = reader.getSigned(DataType.INT).toInt()
    val id = (button shr 16) and 0xFFFF
    val slot = button and 0xFFFF
    return@MessageDecoder ButtonMessage(id, slot)
}

internal val ExtendedButtonMessageDecoder = MessageDecoder(155) { frame ->
    //        case 155: //Interface options
//        case 196:
//        case 124:
//        case 199:
//        case 234:
//        case 168:
//        case 166:
//        case 64:
//        case 53:
//        case 9:
    val reader = GameFrameReader(frame)
    val button = reader.getSigned(DataType.INT).toInt() //data
    val id = (button shr 16) and 0xFFFF //component
    val slot = button and 0xFFFF //buttonId
//        data = buffer.getInt()
//        slot = buffer.getShort()
//        componentId = (data shr 16) and 0xFFFF
//        buttonId = data and 0xFFFF
    return@MessageDecoder ExtendedButtonMessage(id, slot)

}
