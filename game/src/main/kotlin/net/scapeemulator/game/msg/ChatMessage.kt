package net.scapeemulator.game.msg

data class ChatMessage(@JvmField val color: Int, @JvmField val effects: Int, @JvmField val text: String) : Message()
