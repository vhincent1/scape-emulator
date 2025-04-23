package net.scapeemulator.game.io.jdbc

import net.scapeemulator.game.model.Inventory
import net.scapeemulator.game.model.Item
import net.scapeemulator.game.model.Player
import java.io.IOException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Types

abstract class ItemsTable(connection: Connection, private val type: String) : Table() {
    private val loadStatement: PreparedStatement
    private val saveStatement: PreparedStatement

    init {
        this.loadStatement = connection.prepareStatement("SELECT * FROM items WHERE player_id = ? AND type = ?;")
        this.saveStatement =
            connection.prepareStatement("INSERT INTO items (player_id, type, slot, item, amount) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE item = VALUES(item), amount = VALUES(amount);")
    }

    abstract fun getInventory(player: Player): Inventory

    @Throws(SQLException::class, IOException::class)
    override fun load(player: Player) {
        loadStatement.setInt(1, player.databaseId)
        loadStatement.setString(2, type)

        val inventory = getInventory(player)
        loadStatement.executeQuery().use { set ->
            while (set.next()) {
                val slot = set.getInt("slot")
                val item = set.getInt("item")
                val amount = set.getInt("amount")

                if (set.wasNull()) {
                    inventory.set(slot, null)
                } else {
                    inventory.set(slot, Item(item, amount))
                }
            }
        }
    }

    @Throws(SQLException::class, IOException::class)
    override fun save(player: Player) {
        saveStatement.setInt(1, player.databaseId)
        saveStatement.setString(2, type)

        val inventory = getInventory(player)
        val items = inventory.toArray()
        for (slot in items.indices) {
            val item = items[slot]

            saveStatement.setInt(3, slot)
            if (item == null) {
                saveStatement.setNull(4, Types.INTEGER)
                saveStatement.setNull(5, Types.INTEGER)
            } else {
                saveStatement.setInt(4, item.id)
                saveStatement.setInt(5, item.amount)
            }

            saveStatement.addBatch()
        }

        saveStatement.executeBatch()
    }
}
