package net.scapeemulator.game.msg

data class EquipItemMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val itemSlot: Int,
    @JvmField val itemId: Int
) : Message
