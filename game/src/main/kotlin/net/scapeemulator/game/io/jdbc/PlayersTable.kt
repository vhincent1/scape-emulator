package net.scapeemulator.game.io.jdbc

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Position
import org.mindrot.jbcrypt.BCrypt
import java.io.IOException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class PlayersTable(connection: Connection) : Table() {
    private val loadStatement: PreparedStatement = connection.prepareStatement("SELECT * FROM players WHERE id = ?")
    private val saveStatement: PreparedStatement =
        connection.prepareStatement("REPLACE INTO players (id, username, password, rights, x, y, height) VALUES (?, ?, ?, ?, ?, ?, ?);")

    @Throws(SQLException::class, IOException::class)
    override fun load(player: Player) {
        loadStatement.setInt(1, player.databaseId)

        loadStatement.executeQuery().use { set ->
            if (!set.first()) throw IOException()
            player.username = set.getString("username")
            player.rights = set.getInt("rights")

            val x = set.getInt("x")
            val y = set.getInt("y")
            val height = set.getInt("height")
            player.position = Position(x, y, height)
        }
    }

    @Throws(SQLException::class)
    override fun save(player: Player) {
        saveStatement.setInt(1, player.databaseId)
        saveStatement.setString(2, player.username)
        saveStatement.setString(3, BCrypt.hashpw(player.password, BCrypt.gensalt()))
        saveStatement.setInt(4, player.rights)

        val position = player.position
        saveStatement.setInt(5, position.x)
        saveStatement.setInt(6, position.y)
        saveStatement.setInt(7, position.height)

        saveStatement.execute()
    }
}
