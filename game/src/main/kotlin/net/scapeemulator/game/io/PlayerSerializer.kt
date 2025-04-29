package net.scapeemulator.game.io

import net.scapeemulator.game.model.Player

abstract class PlayerSerializer {
    class SerializeResult(val status: Int, val player: Player? = null)

    abstract fun load(username: String, password: String): SerializeResult
    abstract fun save(player: Player)
}
