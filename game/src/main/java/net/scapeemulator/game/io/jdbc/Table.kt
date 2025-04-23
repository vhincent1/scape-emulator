package net.scapeemulator.game.io.jdbc

import net.scapeemulator.game.model.Player
import java.io.IOException
import java.sql.SQLException

abstract class Table {
    @Throws(SQLException::class, IOException::class)
    abstract fun load(player: Player)

    @Throws(SQLException::class, IOException::class)
    abstract fun save(player: Player)
}
