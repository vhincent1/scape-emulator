package net.scapeemulator.game.msg

typealias InteractionType = InteractionMessage.Type

data class InteractionMessage(val type: Type, val index: Int, val option: Int) : Message {
    enum class Type { PLAYER, NPC }
}