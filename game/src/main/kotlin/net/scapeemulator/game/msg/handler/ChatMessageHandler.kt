package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.msg.ChatMessage

internal val ChatMessageHandler = MessageHandler<ChatMessage> { player, message ->
    player.chatMessage = message
}