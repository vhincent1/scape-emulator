package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.msg.GroundItemOptionMessage
import net.scapeemulator.game.msg.ItemDropMessage
import net.scapeemulator.game.util.drop
import net.scapeemulator.game.util.pickup


internal val DropItemMessageHandler = MessageHandler<ItemDropMessage> { player, message ->
    //player.actionsBlocked return
    if (!player.inventory.verify(message.slot, message.id)) return@MessageHandler
    //if(player.inHouse) return
    val item = player.inventory.get(message.slot) ?: return@MessageHandler
    player.drop(item, message.slot)
}

internal val GroundItemOptionMessageHandler = MessageHandler<GroundItemOptionMessage> { player, message ->
    val (id, position, option) = message
    println("option=${option} item=$id pos=[${position.x}, ${position.y}]")
    player.sendMessage("Item: $id")

    if (option == 1) {
        val found = GameServer.WORLD.items.nodes.filterNotNull().find { it.id == id && it.position == position }
        found?.let {
            player.sendMessage("Found: index=${it.index} item=${it.toItem()}")
            player.pickup(it)//if (player.pickup(it)) GameServer.WORLD.items.remove(it)
        }
    }

//    println("LIST:")
//    manager.nodes.filterNotNull()
//        .forEach { i -> println("${i.index} ${i.id} ${i.amount} ${position.x} ${position.y}") }
}