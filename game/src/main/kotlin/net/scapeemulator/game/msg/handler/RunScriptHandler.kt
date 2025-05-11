package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.msg.RunScriptMessage

internal val runScriptHandler = messageHandler<RunScriptMessage> { player, message ->
    val runScript = player.runScript
    runScript?.block?.invoke(player, message.value)
    player.runScript = null
}