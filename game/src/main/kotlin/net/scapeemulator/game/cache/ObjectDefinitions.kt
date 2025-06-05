package net.scapeemulator.game.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.cache.Archive
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container
import net.scapeemulator.cache.ReferenceTable
import net.scapeemulator.cache.def.ObjectDefinition
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
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

fun main() {
    val mapper: ObjectMapper = jacksonObjectMapper()
    // Load from a File
    val path = "./game/src/main/resources/data"
    val file = File("$path/object_configs.json")

    class ObjConfig {
        val ids: String? = null
        val examine: String? = null
    }

    val map: Array<ObjConfig> = mapper.readValue(file, object : TypeReference<Array<ObjConfig>>() {})
    KotlinLogging.logger { }.info { "Loaded ${map.size} object configs" }

    class ObjConfig2 {
        var ids: IntArray? = null
        var examine: String? = null
    }

    val newMap = ArrayList<ObjConfig2>()
    map.forEach { obj ->
        val ids = obj.ids?.split(',')!!.map { it.trim() }  // Trim whitespace around each element
            .mapNotNull { it.toIntOrNull() } // Convert to Int, return null if conversion fails
            .toIntArray() // Convert the list of integers to IntArray
        val a = ObjConfig2()
        a.ids = ids
        a.examine = obj.examine
        newMap.add(a)
    }


//    val map: Array<ObjectDefinition> =
//        mapper.readValue(file, object : TypeReference<Array<ObjectDefinition>>() {})

//    val writer = mapper.writer(DefaultPrettyPrinter())
    val writer = mapper.writer()
    writer?.writeValue(File("$path/objectExamines.json"), newMap.toTypedArray())

}