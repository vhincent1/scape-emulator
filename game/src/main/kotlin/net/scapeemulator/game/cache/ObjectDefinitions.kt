package net.scapeemulator.game.cache

import net.scapeemulator.cache.Archive
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container
import net.scapeemulator.cache.ReferenceTable
import net.scapeemulator.cache.def.ObjectDefinition
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

object ObjectDefinitions {
    private val logger: Logger = LoggerFactory.getLogger(ObjectDefinitions::class.java)
    private lateinit var definitions: Array<ObjectDefinition?>

    @Throws(IOException::class)
    fun init(cache: Cache) {
        var count = 0

        val tableContainer = Container.decode(cache.store.read(255, 16))
        val table = ReferenceTable.decode(tableContainer.getData())

        val files = table.capacity()
        definitions = arrayOfNulls(files * 256)

        for (file in 0 until files) {
            val entry = table.getEntry(file) ?: continue

            val archive = Archive.decode(cache.read(16, file).getData(), entry.size())
            var nonSparseMember = 0
            for (member in 0 until entry.capacity()) {
                val childEntry = entry.getEntry(member) ?: continue

                val id = file * 256 + member
                val definition = ObjectDefinition.decode(
                    archive.getEntry(nonSparseMember++)!!
                )
                definitions[id] = definition
                count++
            }
        }

        logger.info("Loaded $count object definitions.")
    }

    fun count(): Int = definitions.size
    fun forId(id: Int): ObjectDefinition? = definitions[id]
}