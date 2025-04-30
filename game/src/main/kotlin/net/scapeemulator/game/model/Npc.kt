package net.scapeemulator.game.model

class Npc(val type: Int) : Mob() {
    override val isRunning: Boolean
        get() = false

    //npc definitions
}
