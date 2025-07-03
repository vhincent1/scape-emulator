package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.ItemDefinitions
import net.scapeemulator.game.msg.ExamineMessage
import net.scapeemulator.game.msg.ExamineType

internal val ExamineMessageHandler = MessageHandler<ExamineMessage> { player, message ->
    when (message.type) {
        ExamineType.ITEM -> {
            val item = ItemDefinitions.forId(message.id)
            if (item != null) player.sendMessage(item.examine)
        }

        ExamineType.NPC -> {}
        ExamineType.OBJECT -> {}
    }
}