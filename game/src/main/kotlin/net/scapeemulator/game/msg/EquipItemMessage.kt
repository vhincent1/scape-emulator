package net.scapeemulator.game.msg

class EquipItemMessage(
    @JvmField val id: Int,
    @JvmField val slot: Int,
    @JvmField val itemSlot: Int,
    @JvmField val itemId: Int
) : Message()
