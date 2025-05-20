package net.scapeemulator.game.plugin

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.Message

interface PluginEvent

class LoginEvent(val player: Player) : PluginEvent
class ButtonEvent(val player: Player, val buttonId: Int, val slotId: Int) : PluginEvent
class TeleportEvent(player: Player) : PluginEvent
class CharDesignEvent(val player: Player, buttonId: Int) : PluginEvent
class MessageEvent(val player: Player, val message: Message) : PluginEvent