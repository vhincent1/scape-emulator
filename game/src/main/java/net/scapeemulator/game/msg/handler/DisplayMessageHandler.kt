package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.InterfaceSet
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.DisplayMessage

class DisplayMessageHandler : MessageHandler<DisplayMessage>() {
    override fun handle(player: Player, message: DisplayMessage) {
        val interfaces = player.interfaceSet
        val currentMode = interfaces.displayMode
        val newMode = if (message.mode == 0 || message.mode == 1) InterfaceSet.DisplayMode.FIXED
        else InterfaceSet.DisplayMode.RESIZABLE

        if (newMode != currentMode) {
            interfaces.displayMode = newMode
            interfaces.init()
            interfaces.openWindow(Interface.DISPLAY_SETTINGS) // TODO close on the old root?
        }
    }
}
