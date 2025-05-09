package net.scapeemulator.game.msg

import net.scapeemulator.game.msg.codec.Type

class DisplayModelMessage(
    @JvmField val type: Type,
    @JvmField val nodeId: Int,
    @JvmField val amount: Int,
    @JvmField val interfaceId: Int,
    @JvmField val childId: Int
) : Message() {

    @JvmField
    var zoom: Int = 0

    //player
    constructor(interfaceId: Int, childId: Int) :
            this(Type.PLAYER, -1, 0, interfaceId, childId)

    //NPC
    constructor(nodeId: Int, interfaceId: Int, childId: Int) :
            this(Type.NPC, nodeId, 0, interfaceId, childId)

}