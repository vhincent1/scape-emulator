package net.scapeemulator.game.button

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player

class SettingsButtonHandler : ButtonHandler(Interface.SETTINGS) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        val settings = player.settings
        if (slot == 3) {
            settings.toggleRunning()
        } else if (slot == 4) {
            settings.toggleChatFancy()
        } else if (slot == 5) {
            settings.togglePrivateChatSplit()
        } else if (slot == 6) {
            settings.toggleTwoButtonMouse()
        } else if (slot == 7) {
            settings.toggleAcceptingAid()
        } else if (slot == 16) {
            player.interfaceSet
                .openWindow(Interface.DISPLAY_SETTINGS) // TODO needs 'please close the interface...' text?
        } else if (slot == 18) {
            player.interfaceSet.openWindow(Interface.AUDIO_SETTINGS)
        }
    }
}
