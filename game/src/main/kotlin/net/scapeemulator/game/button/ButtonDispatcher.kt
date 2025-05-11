package net.scapeemulator.game.button

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.plugin.ButtonEvent

class ButtonDispatcher {
    private val handlers = HashMap<Int, ButtonHandler>()

    init {
        bind(FixedButtonHandler())
        bind(ResizableButtonHandler())
        bind(LogoutButtonHandler())
        bind(EmoteButtonHandler())
        bind(EnergyOrbButtonHandler())
        bind(SettingsButtonHandler())
        bind(WorldMapButtonHandler())

        bind(AttackButtonHandler(Interface.ATTACK_AXE, intArrayOf(2, 5, 4, 3), 26))
        bind(AttackButtonHandler(Interface.ATTACK_MAUL, intArrayOf(2, 4, 3), 24))
        bind(AttackButtonHandler(Interface.ATTACK_BOW, intArrayOf(2, 4, 3), 27))
        bind(AttackButtonHandler(Interface.ATTACK_CLAWS, intArrayOf(2, 5, 4, 3), 26))
        bind(AttackButtonHandler(Interface.ATTACK_LONGBOW, intArrayOf(2, 4, 3), 24))
        bind(AttackButtonHandler(Interface.ATTACK_FIXED_DEVICE, intArrayOf(2, 3), 7))
        bind(AttackButtonHandler(Interface.ATTACK_GODSWORD, intArrayOf(2, 3, 4, 5), 26))
        bind(AttackButtonHandler(Interface.ATTACK_SWORD, intArrayOf(2, 3, 4, 5), 26))
        bind(AttackButtonHandler(Interface.ATTACK_PICKAXE, intArrayOf(2, 3, 4, 5), 26))
        bind(AttackButtonHandler(Interface.ATTACK_HALBERD, intArrayOf(2, 3, 4, 5), 24))
        bind(AttackButtonHandler(Interface.ATTACK_STAFF, intArrayOf(2, 3, 4), 24))
        bind(AttackButtonHandler(Interface.ATTACK_SCYTHE, intArrayOf(2, 3, 4, 5), 11))
        bind(AttackButtonHandler(Interface.ATTACK_SPEAR, intArrayOf(2, 3, 4, 5), 26))
        bind(AttackButtonHandler(Interface.ATTACK_MACE, intArrayOf(2, 3, 4, 5), 26))
        bind(AttackButtonHandler(Interface.ATTACK_DAGGER, intArrayOf(2, 3, 4, 5), 26))
        bind(AttackButtonHandler(Interface.ATTACK_MAGIC_STAFF, intArrayOf(1, 2, 3), 9))
        bind(AttackButtonHandler(Interface.ATTACK_THROWN, intArrayOf(2, 3, 4), 24))
        bind(AttackButtonHandler(Interface.ATTACK_UNARMED, intArrayOf(2, 3, 4), 24))
        bind(AttackButtonHandler(Interface.ATTACK_WHIP, intArrayOf(2, 3, 4), 24))
    }

    fun bind(handler: ButtonHandler) {
        handlers.put(handler.id, handler)
    }

    fun handle(player: Player, id: Int, slot: Int, parameter: Int) {
        val handler = handlers[id]
//        handler?.handle(player, slot, parameter)
        if (handler != null)
            handler.handle(player, slot, parameter)
        else
            GameServer.plugins.notify(ButtonEvent(player, id, slot))
    }

}
