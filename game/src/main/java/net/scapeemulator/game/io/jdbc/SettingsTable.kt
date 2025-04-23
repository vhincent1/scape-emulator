package net.scapeemulator.game.io.jdbc

import net.scapeemulator.game.model.Player
import java.io.IOException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

class SettingsTable(connection: Connection) : Table() {
    private val loadStatement: PreparedStatement
    private val saveStatement: PreparedStatement

    init {
        this.loadStatement = connection.prepareStatement("SELECT * FROM settings WHERE player_id = ?;")
        this.saveStatement =
            connection.prepareStatement("INSERT INTO settings (player_id, setting, value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value);")
    }

    @Throws(SQLException::class, IOException::class)
    override fun load(player: Player) {
        loadStatement.setInt(1, player.databaseId)

        val settings = player.settings
        loadStatement.executeQuery().use { set ->
            while (set.next()) {
                val setting = set.getString("setting")
                val value = set.getInt("value")

                when (setting) {
                    "attack_style" -> settings.setAttackStyle(value)
                    "auto_retaliating" -> settings.setAutoRetaliating(value != 0)
                    "two_button_mouse" -> settings.setTwoButtonMouse(value != 0)
                    "chat_fancy" -> settings.setChatFancy(value != 0)
                    "private_chat_split" -> settings.setPrivateChatSplit(value != 0)
                    "accepting_aid" -> settings.setAcceptingAid(value != 0)
                    else -> throw IOException("unknown setting: " + setting)
                }
            }
        }
    }

    @Throws(SQLException::class, IOException::class)
    override fun save(player: Player) {
        saveStatement.setInt(1, player.databaseId)

        val settings = player.settings
        saveStatement.setString(2, "attack_style")
        saveStatement.setInt(3, settings.getAttackStyle())
        saveStatement.addBatch()

        saveStatement.setString(2, "auto_retaliating")
        saveStatement.setInt(3, if (settings.isAutoRetaliating()) 1 else 0)
        saveStatement.addBatch()

        saveStatement.setString(2, "two_button_mouse")
        saveStatement.setInt(3, if (settings.isTwoButtonMouse()) 1 else 0)
        saveStatement.addBatch()

        saveStatement.setString(2, "chat_fancy")
        saveStatement.setInt(3, if (settings.isChatFancy()) 1 else 0)
        saveStatement.addBatch()

        saveStatement.setString(2, "private_chat_split")
        saveStatement.setInt(3, if (settings.isPrivateChatSplit()) 1 else 0)
        saveStatement.addBatch()

        saveStatement.setString(2, "accepting_aid")
        saveStatement.setInt(3, if (settings.isAcceptingAid()) 1 else 0)
        saveStatement.addBatch()

        saveStatement.executeBatch()
    }
}
