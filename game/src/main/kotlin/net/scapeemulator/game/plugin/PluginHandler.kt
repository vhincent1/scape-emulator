package net.scapeemulator.game.plugin

import net.scapeemulator.game.button.ButtonHandler
import net.scapeemulator.game.command.CommandHandler

abstract class PluginHandler : PluginEvent() {
    abstract fun handle(event: PluginEvent)
    abstract fun commands(): Array<CommandHandler>
    abstract fun buttons(): Array<ButtonHandler>
}

internal fun pluginHandler(
    block: (PluginEvent) -> Unit,
    init: () -> Unit,
    cmdBlock: Array<CommandHandler> = emptyArray(),
    buttonBlock: Array<ButtonHandler> =  emptyArray(),
): PluginHandler {
    return object : PluginHandler() {
        init {
            init.invoke()
        }

        override fun handle(event: PluginEvent) = block(event)
        override fun commands(): Array<CommandHandler> = cmdBlock
        override fun buttons(): Array<ButtonHandler> = buttonBlock
    }
}

internal fun pluginHandler(
    block: (PluginEvent) -> Unit,
    cmdBlock: Array<CommandHandler> = emptyArray(),
    buttonBlock: Array<ButtonHandler> = emptyArray(),
): PluginHandler {
    return pluginHandler(block, {}, cmdBlock, buttonBlock)
}


