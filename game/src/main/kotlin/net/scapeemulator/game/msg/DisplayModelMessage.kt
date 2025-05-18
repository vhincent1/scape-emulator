package net.scapeemulator.game.msg

class DisplayModelMessage(
    @JvmField val type: Type,
    @JvmField val nodeId: Int,
    @JvmField val amount: Int,
    @JvmField val interfaceId: Int,
    @JvmField val childId: Int
) : Message() {

    enum class Type {
        PLAYER, NPC, ITEM, MODEL
    }

    @JvmField
    var zoom: Int = 0

    //player
    constructor(interfaceId: Int, childId: Int) :
            this(Type.PLAYER, -1, 0, interfaceId, childId)

    //NPC
    constructor(nodeId: Int, interfaceId: Int, childId: Int) :
            this(Type.NPC, nodeId, 0, interfaceId, childId)

}