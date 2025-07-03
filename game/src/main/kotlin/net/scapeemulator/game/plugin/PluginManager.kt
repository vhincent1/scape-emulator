package net.scapeemulator.game.plugin

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.plugin.combat.CombatPlugin.CombatHandler
import net.scapeemulator.game.plugin.rsinterface.CharDesignPlugin
import net.scapeemulator.game.plugin.rsinterface.EquipmentPlugin

class PluginManager(val server: GameServer) {
    private val plugins: MutableList<PluginHandler> = ArrayList()
    private fun add(plugin: PluginHandler) = plugins.add(plugin)

    init {
        add(LoginPlugin)
        add(UtilPlugin(server.world))
        add(CharDesignPlugin)
        add(CombatHandler(server.world))
        add(EquipmentPlugin.plugin)

//        var buttonCount = 0
//        listeners.onEach { buttonCount = it.buttons().size }
        bind()
    }

    //todo check
    fun notify(event: PluginEvent) = plugins.onEach { it.handle(event) }

    private fun bind() = plugins.onEach { listener ->
        listener.buttons().onEach(server.messageDispatcher.buttonDispatcher::bind)
        listener.commands().onEach(server.messageDispatcher.commandDispatcher::bind)
    }
}