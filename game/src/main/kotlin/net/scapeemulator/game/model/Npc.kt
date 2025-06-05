package net.scapeemulator.game.model

class Npc(val type: Int) : Mob() {
    override val isRunning: Boolean
        get() = false

    override fun login() {
        //spawn location
    }

    override fun logout() {
        //despawn
    }
    override fun getClientIndex(): Int = index


    //npc definitions
}
