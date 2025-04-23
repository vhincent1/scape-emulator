package net.scapeemulator.game.msg

class ExtendedButtonMessage(val id: Int, val slot: Int, val parameter: Int, val itemId: Int? = 0) : Message()