package net.scapeemulator.game.plugin

import net.scapeemulator.game.GameServer

class PluginManager(val server: GameServer) {

    private val listeners: MutableList<PluginHandler> = ArrayList()
    private fun addListener(listener: PluginHandler) {
        listeners.add(listener)
    }

    init {
        addListener(loginPlugin)
        addListener(utilPlugin())
        addListener(charDesignPlugin)

//        var buttonCount = 0
//        listeners.forEach { buttonCount = it.buttons().size }
        bind()
    }

    fun notify(event: PluginEvent) = listeners.forEach { listener -> listener.handle(event) }
    private fun bind() = listeners.forEach { listener ->
        listener.buttons().forEach(server.messageDispatcher.buttonDispatcher::bind)
        listener.commands().forEach(server.messageDispatcher.commandDispatcher::bind)
    }
}