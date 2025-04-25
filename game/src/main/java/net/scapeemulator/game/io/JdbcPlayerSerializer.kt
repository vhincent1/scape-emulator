package net.scapeemulator.game.io

import net.scapeemulator.game.io.jdbc.ItemsTable
import net.scapeemulator.game.io.jdbc.PlayersTable
import net.scapeemulator.game.io.jdbc.SettingsTable
import net.scapeemulator.game.io.jdbc.Table
import net.scapeemulator.game.model.Inventory
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.net.login.LoginResponse
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class JdbcPlayerSerializer(url: String, username: String, password: String) : PlayerSerializer(), Closeable {
    private val connection: Connection
    private val loginStatement: PreparedStatement
    private val tables: Array<Table>

    init {
        connection = DriverManager.getConnection(url, username, password)
        connection.autoCommit = false
        loginStatement = connection.prepareStatement("SELECT id, password FROM players WHERE username = ?;")
        tables = arrayOf<Table>(
            PlayersTable(connection),
            SettingsTable(connection),
            object : ItemsTable(connection, "inventory") {
                override fun getInventory(player: Player): Inventory {
                    return player.inventory
                }
            },
            object : ItemsTable(connection, "equipment") {
                override fun getInventory(player: Player): Inventory {
                    return player.equipment
                }
            },
            object : ItemsTable(connection, "bank") {
                override fun getInventory(player: Player): Inventory {
                    return player.bank
                }
            }
        )
    }

    override fun load(username: String, password: String): SerializeResult {
        try {
            loginStatement.setString(1, username)
            loginStatement.executeQuery().use { set ->
                if (set.first()) {
                    val id = set.getInt("id")
                    val hashedPassword = set.getString("password")

                    if (BCrypt.checkpw(password, hashedPassword)) {
                        val player = Player()
                        player.databaseId = id
                        player.password = password /* can't use hashed one in PlayerTable */

                        for (table in tables) table.load(player)

                        return SerializeResult(LoginResponse.STATUS_OK, player)
                    }
                }
                return SerializeResult(LoginResponse.STATUS_INVALID_PASSWORD)
            }
        } catch (ex: SQLException) {
            logger.warn("Loading player $username failed.", ex)
            return SerializeResult(LoginResponse.STATUS_COULD_NOT_COMPLETE)
        } catch (ex: IOException) {
            logger.warn("Loading player $username failed.", ex)
            return SerializeResult(LoginResponse.STATUS_COULD_NOT_COMPLETE)
        }
    }

    override fun save(player: Player) {
        try {
            for (table in tables) table.save(player)

            connection.commit()
        } catch (ex: SQLException) {
            try {
                connection.rollback()
            } catch (innerEx: SQLException) {
                /* ignore rollback failure, not much we can do */
            }

            logger.warn("Saving player " + player.username + " failed.", ex)
        } catch (ex: IOException) {
            try {
                connection.rollback()
            } catch (innerEx: SQLException) {
            }

            logger.warn("Saving player " + player.username + " failed.", ex)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        try {
            connection.close()
        } catch (ex: SQLException) {
            throw IOException(ex)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(JdbcPlayerSerializer::class.java)
    }
}
