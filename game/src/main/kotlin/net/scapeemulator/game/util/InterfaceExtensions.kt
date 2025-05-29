package net.scapeemulator.game.util

import net.scapeemulator.game.button.ButtonHandler
import net.scapeemulator.game.command.CommandHandler
import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.plugin.MessageEvent
import net.scapeemulator.game.plugin.PluginEvent
import net.scapeemulator.game.plugin.PluginHandler

class InterfaceExtensions {
}

fun new() {
    data class InterfaceComponent(val id: Int)

    abstract class InterfacePlugin() {
        //action button packet
        fun handle(component: InterfaceComponent, opcode: Int, button: Int, slot: Int, itemId: Int) {}
    }

    fun openWin(id: Int, block: (Int, Int) -> Unit) {

    }

    openWin(0) { id, slot ->

    }

    openWin(667) { id, slot ->

    }

}

val interfaceMods = object : PluginHandler() {

    override fun handle(event: PluginEvent) {

    }

    override fun commands(): Array<CommandHandler> {
        TODO("Not yet implemented")
    }

    override fun buttons(): Array<ButtonHandler> {
        TODO("Not yet implemented")
    }

}

internal val InterfaceModz = PluginHandler(handler = { event ->
    if (event !is MessageEvent) return@PluginHandler
    if (event.message is ButtonMessage) {

    }
})