package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.msg.ChatMessage

internal val chatMessageHandler = messageHandler<ChatMessage> { player, message ->
    player.chatMessage = message
}