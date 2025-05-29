package net.scapeemulator.game.model

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.cache.Archive
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container
import net.scapeemulator.cache.ReferenceTable.Companion.decode
import net.scapeemulator.cache.def.NPCDefinition
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


object NPCDefinitions {
    private val logger = KotlinLogging.logger { }
    private lateinit var definitions: Array<NPCDefinition?>

    @Throws(IOException::class)
    fun init(cache: Cache) {
        var count = 0
        val tableContainer = Container.decode(cache.store.read(255, 18))
        val table = decode(tableContainer.getData())
        val files = table.capacity()
        definitions = arrayOfNulls(files * 128)

        for (file in 0..<files) {
            val entry = table.getEntry(file) ?: continue
            val archive = Archive.decode(cache.read(18, file).getData(), entry.size())

            var nonSparseMember = 0
            for (member in 0..<entry.capacity()) {
                val childEntry = entry.getEntry(member) ?: continue
                val buffer = archive.getEntry(nonSparseMember++) ?: return
                val id = file * 128 + member
                val definition: NPCDefinition = NPCDefinition.decode(id, buffer)
                definitions[id] = definition
                count++

            }
        }
        logger.info { "Loaded $count NPC definitions" }
    }

    @Throws(IOException::class)
    private fun parseNpcExamination() {
        val reader = BufferedReader(FileReader("data/npc-examine.txt"))
        var read: String?
        var count = 0
        while ((reader.readLine().also { read = it }) != null && count < count()) {
            val parsed: Array<String?> = read!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val npcId = parsed[0]!!.trim { it <= ' ' }.toInt()
            val examine = parsed[1]!!.trim { it <= ' ' }
            if (npcId < count()) {
                if (definitions[npcId] != null) {
                    //      definitions[npcId].setExamine(examine)
                }
            }
            count++
        }
        reader.close()
        //  logger.info("Loaded " + count + " NPC examination information.")
    }

    fun count(): Int = definitions.size

    fun forId(id: Int): NPCDefinition? {
        try {
            return definitions[id]
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            return null
        }
    }
}

fun main() {
    val logger = KotlinLogging.logger { }
    val path = "./game/src/main/resources/data"
    val file = File("$path/npcDefinitions.json")
    val mapper: ObjectMapper = jacksonObjectMapper()

    val definitions: Array<NPCDefinition> = mapper.readValue(file,
        object : TypeReference<Array<NPCDefinition>>() {})

    //todo convert protectStyle to String

//    val writer = mapper.writer(DefaultPrettyPrinter())
//    writer?.writeValue(File("$path/npcDefinitions-new.json"), a)

    logger.info { "Loaded ${definitions.size} npc definitions" }
//    println(definitions[1]?.config?.get("respawn"))
//    println(a.find { it.id == 50 }?.combat_audio)
}