package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.ChatMessage

class ChatMessageHandler : MessageHandler<ChatMessage>() {
    override fun handle(player: Player, message: ChatMessage) {
        player.chatMessage = message
    }
}
