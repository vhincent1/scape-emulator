package net.scapeemulator.game.msg

//type = isWalkable
class InterfaceOpenMessage(val id: Int, val slot: Int, val childId: Int, val type: Int) : Message()
