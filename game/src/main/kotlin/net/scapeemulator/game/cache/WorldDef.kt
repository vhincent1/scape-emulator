package net.scapeemulator.game.cache

import net.scapeemulator.cache.*
import net.scapeemulator.cache.util.ByteBufferUtils
import net.scapeemulator.cache.util.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.ByteBuffer

//todo https://rune-server.org/threads/some-world-map-info.692915/
//todo https://rune-server.org/threads/some-map-scene-info.693083/

object WorldDef {
    private val logger: Logger = LoggerFactory.getLogger(ObjectDefinitions::class.java)
    private lateinit var definitions: Array<WorldMapDetailsType?>

    @Throws(IOException::class)
    fun init(cache: Cache) {
        var count = 0

//        val tableContainer = Container.decode(cache.store.read(255, 23))
//        val table = ReferenceTable.decode(tableContainer.getData())

        val container = Container.decode(cache.store.read(255, 23)).getData()
        val table = ReferenceTable.decode(container)
        val files = table.capacity()
        for (file in 0 until files) {
            val entry = table.getEntry(file) ?: continue
            val fileId = StringUtils.hash("details")

            println("Entry2: ${entry.identifier}")

            when (entry.identifier) {
                fileId -> {

//                    val archive = Archive.decode(cache.read(23, file).getData(), entry.size())
                    var nonSparseMember = 0

                    fun decode(buffer: ByteBuffer) {
                        val s = ByteBufferUtils.getString(buffer)
                        println(s)
                        println(ByteBufferUtils.getString(buffer))
                        val regionhash = buffer.getInt()
                        val xcoord = (regionhash shr 14 and 0x3fff)
                        val ycoord = (regionhash and 0x3fff)
                        println("$regionhash")
                    }

                    for (member in 0 until entry.capacity()) {
//                        val childEntry = entry.getEntry(member) ?: continue
////                        val data = archive.getEntry(nonSparseMember++)
////                        decode(data!!)
                        val data = cache.read(23, file/*member*/).getData()
                        decode(data)
                    }


//                    val data = cache.read(23, member/*member*/).getData()
//                    decode(data)
                }
            }

//        for (int member = 0; member <= capacity; member++) {
//            byte[] data = index . getFile (index.getArchiveId("details"), member);
//        }

        }

        logger.info("Loaded $count  definitions.")
    }

    fun count(): Int = definitions.size
//    fun forId(id: Int): ObjectDefinition? = definitions[id]
}

fun main() {

    WorldDef.init(Cache(FileStore.Companion.open("./game/src/main/resources/data/cache")))
}

class WorldMapDetailsType {
    private var id = 0

    // first section?
    var aBool5830 = false
    var anInt5833 = 0
    var anInt5836 = 0
    var anInt5838 = 0
    var anInt5840 = 0
    var anInt5841 = 0
    var anInt5842 = 0
    var areaName: String? = null
    var anInt5845 = 0
    var staticelementsname: String? = null
    var regionhash = 0
    var xcoord = 0
    var ycoord = 0
    var bestbottomleftX = 0
    var bestbottomleftY = 0
    var besttoprightX = 0
    var besttoprightY = 0
    var plane = 0
    var bottomleftX = 0
    var bottomleftY = 0
    var toprightX = 0
    var toprightY = 0

    fun main() {
        // 2304 1664
        val i = 52956320
        val x = (i shr 14 and 0x3fff)
        val y = (i and 0x3fff)
        println("$x, $y")
    }

    fun WorldMapDetailsType(id: Int) {
        this.id = id
        this.anInt5840 = -1
        this.anInt5833 = 12800
        this.anInt5838 = 0
        this.anInt5841 = -1
        this.aBool5830 = true
        this.anInt5842 = 12800
    }

    @Suppress("unused")
    fun decode(buffer: ByteBuffer) {
        /////////////
        // first part
        this.staticelementsname = ByteBufferUtils.getString(buffer)
        this.areaName = ByteBufferUtils.getString(buffer)
        this.regionhash = buffer.getInt()

        // this wasnt in client but it helps better in dumps.
        this.xcoord = (regionhash shr 14 and 0x3fff)
        this.ycoord = (regionhash and 0x3fff)

        this.anInt5841 = buffer.getInt()
        this.aBool5830 = (buffer.get().toInt() and 0xff) === 1
        this.anInt5840 = buffer.get().toInt() and 0xff

        buffer.get() // unused

        if (anInt5840 == 255) anInt5840 = 0

        //////////////
        // second part
        val length: Int = buffer.get().toInt() and 0xff
        for (index in 0 until length) {
            this.plane = buffer.get().toInt() and 0xff
            this.bottomleftX = buffer.getShort().toInt() and 0xffff
            this.bottomleftY = buffer.getShort().toInt() and 0xffff
            this.toprightX = buffer.getShort().toInt() and 0xffff
            this.toprightY = buffer.getShort().toInt() and 0xffff
            // the rest might have something to do with the coords flipped.
            this.bestbottomleftX = buffer.getShort().toInt() and 0xffff
            this.bestbottomleftY = buffer.getShort().toInt() and 0xffff
            this.besttoprightX = buffer.getShort().toInt() and 0xffff
            this.besttoprightY = buffer.getShort().toInt() and 0xffff
            var e = buffer.getShort().toInt() and 0xffff
        }
    }

//    fun encode(): ByteBuffer? {
//        // TODO Auto-generated method stub
//        return null
//    }

}