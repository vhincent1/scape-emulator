package net.scapeemulator.game.plugin

import net.scapeemulator.game.button.ButtonHandler
import net.scapeemulator.game.command.CommandHandler

abstract class PluginHandler : PluginEvent {
    abstract fun handle(event: PluginEvent)
    abstract fun commands(): Array<CommandHandler>
    abstract fun buttons(): Array<ButtonHandler>
}

internal fun PluginHandler(
    block: (PluginEvent) -> Unit,
    init: () -> Unit,
    commandHandlers: Array<CommandHandler> = emptyArray(),
    buttonHandlers: Array<ButtonHandler> = emptyArray(),
): PluginHandler {
    return object : PluginHandler() {
        init {
            init.invoke()
        }

        override fun handle(event: PluginEvent) = block(event)
        override fun commands(): Array<CommandHandler> = commandHandlers
        override fun buttons(): Array<ButtonHandler> = buttonHandlers
    }
}

internal fun PluginHandler(
    block: (PluginEvent) -> Unit,
    commandHandlers: Array<CommandHandler> = emptyArray(),
    buttonHandlers: Array<ButtonHandler> = emptyArray(),
): PluginHandler {
    return PluginHandler(block, {}, commandHandlers, buttonHandlers)
}


