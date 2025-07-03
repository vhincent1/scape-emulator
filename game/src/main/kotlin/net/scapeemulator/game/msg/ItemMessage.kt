package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position

data class ItemDropMessage(val id: Int, val slot: Int) : Message
data class ItemOnGroundItemMessage(
    val x: Int,
    val y: Int,
    val slot: Int,
    val item: Int,
    val groundItem: Int,
    val widget: Int
) : Message

data class ItemOnItemMessage(
    val item1: Int,
    val slot1: Int,
    val hash1: Int,
    val item2: Int,
    val slot2: Int,
    val hash2: Int
) : Message

data class ItemOnNPCMessage(val itemId: Int, val slot: Int, val target: Int, val widget: Int) : Message
data class ItemOnObjectMessage(
    val itemId: Int,
    val slot: Int,
    val widget: Int,
    val objectId: Int,
    val position: Position
) : Message

data class MagicOnItemMessage(val tabId: Int, val spellId: Int, val slot: Int, val itemId: Int) : Message