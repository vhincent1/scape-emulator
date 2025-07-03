package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position

data class GroundItemMessage(
    val type: Type,
    val id: Int,
    val amount: Int,
    val position: Position,
    val previousAmount: Int
) : Message {
    enum class Type { CREATE, UPDATE, REMOVE }

    //update
    constructor(id: Int, amount: Int, position: Position, previousAmount: Int) :
            this(Type.UPDATE, id, amount, position, previousAmount)

    //create
    constructor(id: Int, amount: Int, position: Position) :
            this(Type.CREATE, id, amount, position, -1)

    //remove
    constructor(id: Int, position: Position) :
            this(Type.REMOVE, id, -1, position, -1)
}

//data class GroundItemUpdateMessage(val id: Int, val amount: Int, val position: Position, val previousAmount: Int) :
//    Message
//data class GroundItemCreateMessage(val id: Int, val amount: Int, val position: Position) : Message
//data class GroundItemRemoveMessage(val id: Int, val position: Position) : Message
data class GroundItemOptionMessage(val id: Int, val position: Position, val option: Int) : Message