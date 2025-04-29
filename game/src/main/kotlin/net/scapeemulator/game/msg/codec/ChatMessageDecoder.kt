package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.ChatMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameReader
import net.scapeemulator.util.ChatUtils
import java.io.IOException

class ChatMessageDecoder : MessageDecoder<ChatMessage>(237) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): ChatMessage {
        val reader = GameFrameReader(frame)
        val size = reader.length - 2

        val color = reader.getUnsigned(DataType.BYTE).toInt()
        val effects = reader.getUnsigned(DataType.BYTE).toInt()

        val bytes = ByteArray(size)
        reader.getBytes(bytes)
        val text = ChatUtils.unpack(bytes)

        return ChatMessage(color, effects, text)
    }
}
