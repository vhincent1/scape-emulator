package net.scapeemulator.game.plugin

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.Message

interface PluginEvent
data class LoginEvent(val player: Player) : PluginEvent
data class ButtonEvent(val player: Player, val buttonId: Int, val slotId: Int) : PluginEvent
data class TeleportEvent(val player: Player) : PluginEvent
data class CharDesignEvent(val player: Player, val buttonId: Int) : PluginEvent
data class MessageEvent(val player: Player, val message: Message) : PluginEvent