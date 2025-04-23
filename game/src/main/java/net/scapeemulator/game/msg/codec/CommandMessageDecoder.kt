package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.CommandMessage
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import java.io.IOException

class CommandMessageDecoder : MessageDecoder<CommandMessage>(44) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): CommandMessage {
        val reader = GameFrameReader(frame)
        val command = reader.string
        return CommandMessage(command)
    }
}
