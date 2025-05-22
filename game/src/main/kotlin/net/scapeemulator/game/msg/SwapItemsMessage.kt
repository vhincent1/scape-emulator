package net.scapeemulator.game.msg

data class SwapItemsMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val source: Int,
    @JvmField val destination: Int,
    val type: Int
) : Message()
