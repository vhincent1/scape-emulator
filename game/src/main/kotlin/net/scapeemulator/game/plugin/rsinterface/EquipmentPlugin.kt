package net.scapeemulator.game.plugin.rsinterface

import net.scapeemulator.game.model.InterfaceSet
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.ButtonMessage
import net.scapeemulator.game.plugin.MessageEvent
import net.scapeemulator.game.plugin.PluginHandler
import net.scapeemulator.game.util.sendString

object EquipmentPlugin {
    private const val EQUIPMENT_INTERFACE = 667

    internal val plugin = PluginHandler(handler = { event ->
        if (event !is MessageEvent) return@PluginHandler
        val (player, message) = event
        if (message !is ButtonMessage) return@PluginHandler
        val (button, slot) = message
        player.sendMessage("button=${button} slot=${slot}")
        /* equipment slot */
        if (button == 387 && slot == 55) {
            player.interfaceSet.openWindow(EQUIPMENT_INTERFACE)
            player.interfaceSet.updateEquipmentInterface()
        }
    })

    fun updateBonuses(player: Player) {
        val bonuses = IntArray(15)
        player.equipment.toArray().filterNotNull().forEach { item ->
            val def = item.definition ?: return
            val defBonus = def.bonuses ?: return
            for (i in 0 until defBonus.count()) {
                if (i == 14 && bonuses[i] != 0) continue
                bonuses[i] += defBonus[i]
            }
        }
//        val shield = player.equipment.get(Equipment.SHIELD)?.let { item ->
//            if(item.id == 11283){
//                val increase = item.getCharge // 20
//                bonuses[5] += increase
//                bonuses[6] += increase
//                bonuses[7] += increase
//                bonuses[8] += increase
//                bonuses[9] += increase
//            }
//        }
        player.skillSet.bonuses = bonuses
    }

    private val BONUS_NAMES: Array<String> = arrayOf(
        "Stab: ",
        "Slash: ",
        "Crush: ",
        "Magic: ",
        "Ranged: ",
        "Stab: ",
        "Slash: ",
        "Crush: ",
        "Magic: ",
        "Ranged: ",
        "Summoning: ",
        "Strength: ",
        "Prayer: "
    )

    private fun InterfaceSet.updateEquipmentInterface() {
        if (current != EQUIPMENT_INTERFACE) return
        // player.send(WeightMessage(42.0))
        var index = 0
        val bonuses = player.skillSet.bonuses//arrayOfNulls<Int>(15) //player.getBonuses
        bonuses.fill(42.rangeTo(69).random())
        player.sendString(EQUIPMENT_INTERFACE, 32, "%1Kg");
        player.sendString(EQUIPMENT_INTERFACE, 34, "Attack BONUS")
        for (i in 36..49) {
            if (i == 47) continue
            val bonus: Int = bonuses.getOrNull(index) ?: -1
            val value = if (bonus > -1) ("+$bonus") else bonus
            player.sendString(EQUIPMENT_INTERFACE, i, BONUS_NAMES[index++] + value)
        }
    }
}

