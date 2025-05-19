package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.msg.RunScriptMessage

internal val RunScriptHandler = MessageHandler<RunScriptMessage> { player, message ->
    val runScript = player.runScript
    runScript?.block?.invoke(player, message.value)
    player.runScript = null
}