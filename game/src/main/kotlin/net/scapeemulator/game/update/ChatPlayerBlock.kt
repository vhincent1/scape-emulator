package net.scapeemulator.game.update

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.ChatMessage
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder
import net.scapeemulator.util.ChatUtils

class ChatPlayerBlock(player: Player) : PlayerBlock(0x80) {
    private val chatMessage: ChatMessage?
    private val rights: Int

    init {
        this.chatMessage = player.chatMessage
        this.rights = player.rights
    }

    public override fun encode(message: PlayerUpdateMessage, builder: GameFrameBuilder) {
        val bytes = ByteArray(256)
        val size = ChatUtils.pack(chatMessage!!.text, bytes)

        builder.put(DataType.SHORT, DataOrder.LITTLE, (chatMessage.color shl 8) or chatMessage.effects)
        builder.put(DataType.BYTE, rights)
        builder.put(DataType.BYTE, size)
        builder.putBytesReverse(bytes.copyOf(size))
    }
}
