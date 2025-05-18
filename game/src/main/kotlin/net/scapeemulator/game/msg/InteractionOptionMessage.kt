package net.scapeemulator.game.msg

class InteractionOptionMessage(@JvmField val position: Int, @JvmField val name: String) : Message()

class InteractionMessage(val type: InteractionType, val index: Int, val option: Int) : Message() {
    enum class InteractionType { PLAYER, NPC }
}