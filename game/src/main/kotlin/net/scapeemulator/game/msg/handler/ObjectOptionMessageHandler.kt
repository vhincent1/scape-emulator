package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.msg.ObjectOptionMessage

internal val ObjectOptionMessageHandler = MessageHandler<ObjectOptionMessage> { player, message ->
    player.sendMessage("index=${message.index} obj=" + message.id + " x=" + message.x)
}