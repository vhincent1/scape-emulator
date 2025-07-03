package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position

data class GroundObjectMessage(
    val type: Type,
    val position: Position,
    val id: Int,
    val objType: Int,
    val rotation: Int,
    val animation: Int
) : Message {
    enum class Type { ANIMATE, REMOVE, CREATE }

    constructor(type: Type, id: Int, position: Position) : this(type, position, id, 10, 0, -1)
    constructor(id: Int, position: Position, animation: Int) : this(Type.ANIMATE, position, id, 10, 0, animation)

    fun hash() = objType shl 2 or (rotation and 0x3)
//    fun hash() = (objType shl 2) + (rotation and 3)
}
