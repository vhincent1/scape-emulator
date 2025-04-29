package net.scapeemulator.game.model

import com.github.michaelbull.logging.InlineLogger
import net.scapeemulator.cache.Archive
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container
import net.scapeemulator.cache.ReferenceTable
import net.scapeemulator.cache.def.ItemDefinition
import java.io.IOException

object ItemDefinitions {
    private val logger = InlineLogger(ItemDefinitions::class)
    private lateinit var definitions: Array<ItemDefinition?>

    @JvmStatic
    @Throws(IOException::class)
    fun init(cache: Cache) {
        var count = 0

        val tableContainer = Container.decode(cache.store.read(255, 19))
        val table = ReferenceTable.decode(tableContainer.getData())

        val files = table.capacity()
        definitions = arrayOfNulls(files * 256)

        for (file in 0..<files) {
            val entry = table.getEntry(file)
            if (entry == null) continue

            val archive = Archive.decode(cache.read(19, file).getData(), entry.size())
            var nonSparseMember = 0
            for (member in 0..<entry.capacity()) {
                val childEntry = entry.getEntry(member)
                if (childEntry == null) continue

                val id = file * 256 + member
                val definition = ItemDefinition.decode(archive.getEntry(nonSparseMember++)!!)
                definitions[id] = definition
                count++
            }
        }

        logger.info { "Loaded $count item definitions." }
    }

    fun count(): Int {
        return definitions.size
    }

    fun forId(id: Int): ItemDefinition? {
        return definitions[id]
    }
}
