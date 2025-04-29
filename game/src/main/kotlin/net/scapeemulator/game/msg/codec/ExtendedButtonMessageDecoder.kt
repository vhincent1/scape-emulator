package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ExtendedButtonMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import java.io.IOException

class ExtendedButtonMessageDecoder : MessageDecoder<ExtendedButtonMessage>(155) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): ExtendedButtonMessage {
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
        val parameter = reader.getUnsigned(DataType.SHORT).toInt()

//        data = buffer.getInt()
//        slot = buffer.getShort()
//        componentId = (data shr 16) and 0xFFFF
//        buttonId = data and 0xFFFF
        return ExtendedButtonMessage(id, slot, parameter)
    }
}

//
//class InterfaceOptionMessageDecoder : MessageDecoder<ExtendedButtonMessage>(156) {
//    //case 156:
////slot = buffer.getLEShortA();
////itemId = buffer.getShortA();
////data = buffer.getLEInt();
////componentId = data >> 16;
////buttonId = data & 0xffff;
//    override fun decode(frame: GameFrame): ExtendedButtonMessage {
//
//        System.out.println("afinsanisda "+frame.opcode)
//        val reader = GameFrameReader(frame)
//        val slot: Int = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
//        val itemId: Int = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
//        //LEInt
//        val data: Int = reader.getUnsigned(DataType.BYTE, DataOrder.LITTLE).toInt()
//        val componentId = data shr 16
//        val buttonId = data and 0xffff
//        return ExtendedButtonMessage(buttonId, slot, componentId, itemId)
//    }
//
//}

