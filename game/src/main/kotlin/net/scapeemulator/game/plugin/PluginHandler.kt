package net.scapeemulator.game.plugin

import net.scapeemulator.game.button.ButtonHandler
import net.scapeemulator.game.command.CommandHandler

abstract class PluginHandler : PluginEvent {
    abstract fun handle(event: PluginEvent)
    abstract fun commands(): Array<CommandHandler>
    abstract fun buttons(): Array<ButtonHandler>
}

internal fun PluginHandler(
    handler: (PluginEvent) -> Unit,
    init: () -> Unit = {},
    commands: Array<CommandHandler> = emptyArray(),
    buttons: Array<ButtonHandler> = emptyArray(), //todo remove?
): PluginHandler {
    return object : PluginHandler() {
        init {
            init.invoke()
        }

        override fun handle(event: PluginEvent) = handler(event)
        override fun commands(): Array<CommandHandler> = commands
        override fun buttons(): Array<ButtonHandler> = buttons
    }
}

internal fun PluginHandler(
    block: (PluginEvent) -> Unit,
    commandHandlers: Array<CommandHandler> = emptyArray(),
    buttonHandlers: Array<ButtonHandler> = emptyArray(), //todo remove?
): PluginHandler {
    return PluginHandler(block, {}, commandHandlers, buttonHandlers)
}


