package net.scapeemulator.game.msg

data class ClickMessage(val time: Int, val x: Int, val y: Int, val isRightClick: Boolean) : Message
