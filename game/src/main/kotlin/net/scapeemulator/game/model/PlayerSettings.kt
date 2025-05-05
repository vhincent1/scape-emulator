package net.scapeemulator.game.model

import net.scapeemulator.game.msg.ConfigMessage
import net.scapeemulator.game.msg.ScriptMessage

class PlayerSettings(val player: Player) {
    var attackStyle = 0
        set(value) {
            field = value
            refreshAttackStyle()
        }

    var specialToggled = false
    var specialEnergy = 100
        set(value) {
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

    //    fun setRunning(running: Boolean) {
//        this.running = running
//        refreshRunning()
//    }
//
//    fun isRunning(): Boolean {
//        return running
//    }
//
    fun toggleRunning() {
        running = !running
        refreshRunning()
    }

    //
//    fun setAttackStyle(attackStyle: Int) {
//        this.attackStyle = attackStyle
//        refreshAttackStyle()
//    }
//
//    fun getAttackStyle(): Int {
//        return attackStyle
//    }
//
    fun toggleAutoRetaliating() {
        autoRetaliating = !autoRetaliating
        refreshAutoRetaliating()
    }

    //
//    fun setAutoRetaliating(autoRetaliating: Boolean) {
//        this.autoRetaliating = autoRetaliating
//        refreshAutoRetaliating()
//    }
//
//    fun isAutoRetaliating(): Boolean {
//        return autoRetaliating
//    }
//
//    fun setTwoButtonMouse(twoButtonMouse: Boolean) {
//        this.twoButtonMouse = twoButtonMouse
//        refreshTwoButtonMouse()
//    }
//
    fun toggleTwoButtonMouse() {
        twoButtonMouse = !twoButtonMouse
        refreshTwoButtonMouse()
    }

    //
//    fun isTwoButtonMouse(): Boolean {
//        return twoButtonMouse
//    }
//
//    fun setChatFancy(chatFancy: Boolean) {
//        this.chatFancy = chatFancy
//        refreshChatFancy()
//    }
//
    fun toggleChatFancy() {
        chatFancy = !chatFancy
        refreshChatFancy()
    }

    //
//    fun isChatFancy(): Boolean {
//        return chatFancy
//    }
//
//    fun setPrivateChatSplit(privateChatSplit: Boolean) {
//        this.privateChatSplit = privateChatSplit
//        refreshPrivateChatSplit()
//    }
//
    fun togglePrivateChatSplit() {
        privateChatSplit = !privateChatSplit
        refreshPrivateChatSplit()
    }

    //
//    fun isPrivateChatSplit(): Boolean {
//        return privateChatSplit
//    }
//
//    fun setAcceptingAid(acceptingAid: Boolean) {
//        this.acceptingAid = acceptingAid
//        refreshAcceptingAid()
//    }
//
    fun toggleAcceptingAid() {
        acceptingAid = !acceptingAid
        refreshAcceptingAid()
    }
//
//    fun isAcceptingAid(): Boolean {
//        return acceptingAid
//    }

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

    private fun refreshRunning() {
        player.send(ConfigMessage(173, if (running) 1 else 0))
    }

    private fun refreshAttackStyle() {
        player.send(ConfigMessage(43, attackStyle))
    }

    private fun refreshAutoRetaliating() {
        player.send(ConfigMessage(172, if (autoRetaliating) 0 else 1))
    }

    private fun refreshTwoButtonMouse() {
        player.send(ConfigMessage(170, if (twoButtonMouse) 0 else 1))
    }

    private fun refreshChatFancy() {
        player.send(ConfigMessage(171, if (chatFancy) 0 else 1))
    }

    private fun refreshPrivateChatSplit() {
        player.send(ConfigMessage(287, if (privateChatSplit) 1 else 0))

        if (privateChatSplit) {
            player.send(ScriptMessage(83, "")) // TODO check (Xeno uses type of s but blank params array?)
        }
    }

    private fun refreshAcceptingAid() {
        player.send(ConfigMessage(427, if (acceptingAid) 1 else 0))
    }

    fun refreshSpecialBar() {
        player.send(ConfigMessage(300, specialEnergy * 10))
        player.send(ConfigMessage(301, if (specialToggled) 1 else 0))
    }

    fun setSpecToggle() {
        specialToggled = !specialToggled
        refreshSpecialBar()
    }

}
