package net.scapeemulator.game.msg

typealias Audio = AudioMessage

data class AudioMessage(val id: Int, val volume: Int = 10, val delay: Int = 0) : Message