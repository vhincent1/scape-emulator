package net.scapeemulator.game.plugin

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.plugin.combat.CombatPlugin.CombatHandler

class PluginManager(val server: GameServer) {

    private val listeners: MutableList<PluginHandler> = ArrayList()
    private fun addListener(listener: PluginHandler) {
        listeners.add(listener)
    }

    init {
        addListener(LoginPlugin)
        addListener(UtilPlugin(server.world))
        addListener(CharDesignPlugin)
        addListener(CombatHandler(server.world))

//        var buttonCount = 0
//        listeners.forEach { buttonCount = it.buttons().size }
        bind()
    }

    //todo check
    fun notify(event: PluginEvent) {
        listeners.onEach { listener -> listener.handle(event) }
    }

    private fun bind() = listeners.forEach { listener ->
        listener.buttons().forEach(server.messageDispatcher.buttonDispatcher::bind)
        listener.commands().forEach(server.messageDispatcher.commandDispatcher::bind)
    }
}