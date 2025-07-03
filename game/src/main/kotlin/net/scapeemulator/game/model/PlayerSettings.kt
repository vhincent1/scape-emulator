package net.scapeemulator.game.model

import net.scapeemulator.game.msg.ConfigMessage
import net.scapeemulator.game.msg.ScriptMessage

class PlayerSettings(val player: Player) {
    var skullIcon = -1
        set(value) {
            if (value <= 7) field = value
        }
    var weaponClass: WeaponClass? = null
    var attackStyle = 0
        set(value) {
            field = value
            refreshAttackStyle()
        }

    var specialToggled = false
    var specialEnergy = 100
        set(value) {
            if (field < 0) field = 0
            if (field > 100) field = 100
            field = value
            refreshSpecialBar()
        }
    var autoRetaliating = true
        set(value) {
            field = value
            refreshAutoRetaliating()
        }
    var twoButtonMouse = true
        set(value) {
            field = value
            refreshTwoButtonMouse()
        }
    var chatFancy = true
        set(value) {
            field = value
            refreshChatFancy()
        }
    var privateChatSplit = false
        set(value) {
            field = value
            refreshPrivateChatSplit()
        }
    var acceptingAid = false
        set(value) {
            field = value
            refreshAcceptingAid()
        }
    var running = false /* TODO move to Player as it isn't saved */
        set(value) {
            field = value
            refreshRunning()
        }

    fun toggleRunning() {
        running = !running
        refreshRunning()
    }

    fun toggleAutoRetaliating() {
        autoRetaliating = !autoRetaliating
        refreshAutoRetaliating()
    }

    fun toggleTwoButtonMouse() {
        twoButtonMouse = !twoButtonMouse
        refreshTwoButtonMouse()
    }

    fun toggleChatFancy() {
        chatFancy = !chatFancy
        refreshChatFancy()
    }

    fun togglePrivateChatSplit() {
        privateChatSplit = !privateChatSplit
        refreshPrivateChatSplit()
    }

    fun toggleAcceptingAid() {
        acceptingAid = !acceptingAid
        refreshAcceptingAid()
    }

    fun refresh() {
        refreshRunning()
        refreshAttackStyle()
        refreshAutoRetaliating()
        refreshTwoButtonMouse()
        refreshChatFancy()
        refreshPrivateChatSplit()
        refreshAcceptingAid()
        refreshSpecialBar()
    }

    private fun refreshRunning() = player.send(ConfigMessage(173, if (running) 1 else 0))
    private fun refreshAttackStyle() = player.send(ConfigMessage(43, attackStyle))
    private fun refreshAutoRetaliating() = player.send(ConfigMessage(172, if (autoRetaliating) 0 else 1))
    private fun refreshTwoButtonMouse() = player.send(ConfigMessage(170, if (twoButtonMouse) 0 else 1))
    private fun refreshChatFancy() = player.send(ConfigMessage(171, if (chatFancy) 0 else 1))
    private fun refreshPrivateChatSplit() {
        player.send(ConfigMessage(287, if (privateChatSplit) 1 else 0))
        if (privateChatSplit) player.send(
            ScriptMessage(
                83,
                ""
            )
        ) // TODO check (Xeno uses type of s but blank params array?)
    }

    private fun refreshAcceptingAid() = player.send(ConfigMessage(427, if (acceptingAid) 1 else 0))
    fun refreshSpecialBar() {
        player.send(ConfigMessage(300, specialEnergy * 10))
        player.send(ConfigMessage(301, if (specialToggled) 1 else 0))
    }

    fun toggleSpecialBar() {
        specialToggled = !specialToggled
        refreshSpecialBar()
    }
}
