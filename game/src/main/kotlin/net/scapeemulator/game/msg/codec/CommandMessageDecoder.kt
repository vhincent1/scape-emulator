package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.CommandMessage
import net.scapeemulator.game.net.game.GameFrameReader

//class CommandMessageDecoder : MessageDecoder<CommandMessage>(44) {
//    @Throws(IOException::class)
//    override fun decode(frame: GameFrame): CommandMessage {
//        val reader = GameFrameReader(frame)
//        val command = reader.string
//        return CommandMessage(command)
//    }
//}

internal val CommandMessageDecoder = MessageDecoder(44) { frame ->
    val reader = GameFrameReader(frame)
    val command = reader.string
    return@MessageDecoder CommandMessage(command)
}
