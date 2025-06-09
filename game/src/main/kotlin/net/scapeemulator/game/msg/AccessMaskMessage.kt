package net.scapeemulator.game.msg

data class AccessMaskMessage(
    val id: Int, val child: Int, val interfaceId: Int, val offset: Int, val length: Int
) : Message