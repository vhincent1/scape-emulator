package net.scapeemulator.game.model

class Npc(@JvmField val type: Int) : Mob() {
    override val isRunning: Boolean
        get() = false
}
