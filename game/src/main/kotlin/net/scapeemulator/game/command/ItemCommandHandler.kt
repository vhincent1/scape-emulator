package net.scapeemulator.game.command

import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.Player

class ItemCommandHandler : CommandHandler("item") {
    override fun handle(player: Player, arguments: Array<String>) {
        if (player.rights < 2) return

        if (arguments.size != 1 && arguments.size != 2) {
            player.sendMessage("Syntax: ::item [id] [amount=1]")
            return
        }

        val id = arguments[0].toInt()
        var amount = 1
        if (arguments.size == 2) {
            amount = arguments[1].toInt()
        }

        val item = Item(id, amount)
        player.inventory.add(item)
        val def = item.definition
        if (def != null) {
            println(def.name)
//            println(def.isUnnoted)
//            def.inventoryOptions.forEach { println(it) }
        }

        val def2 = item.equipmentDefinition
        if (def2 != null) {
//            println(def2.isFullHelm())
//            println(def2.isFullBody())
            println(def2.slot)
        }

    }
}
