package net.scapeemulator.game.cache

import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container
import net.scapeemulator.cache.ReferenceTable
import net.scapeemulator.cache.util.ByteBufferUtils
import net.scapeemulator.cache.util.StringUtils
import net.scapeemulator.game.model.ObjectType
import net.scapeemulator.game.model.Position
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

class MapSet {
    private val logger = KotlinLogging.logger {}
    val listeners: MutableList<MapListener> = mutableListOf()

    @Throws(IOException::class)
    fun init(cache: Cache, keyTable: LandscapeKeyTable) {
        logger.info { "Reading map and landscape files..." }
        val rt = ReferenceTable.decode(Container.decode(cache.store.read(255, 5)).getData())
        for (x in 0..255) {
            for (y in 0..255) {
                val landscapeFile = "l" + x + "_" + y
                val mapFile = "m" + x + "_" + y
                val npc = "n" + x + "_" + y //todo check

                val landscapeIdentifier = StringUtils.hash(landscapeFile)
                val mapIdentifier = StringUtils.hash(mapFile)
                val npcId = StringUtils.hash(npc)

                for (id in 0..<rt.capacity()) {
                    val entry = rt.getEntry(id)
                    if (entry == null) continue

                    try {
                        if (entry.identifier == landscapeIdentifier)
                            readLandscape(cache, keyTable, x, y, id)
                        else if (entry.identifier == mapIdentifier)
                            readMap(cache, x, y, id)
                        else if (entry.identifier == npcId)
                            readNpc(cache, x, y, id)
                    } catch (ex: Exception) {
                        logger.debug(ex) { "Failed to read map/landscape file $x, $y." }
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun readLandscape2(cache: Cache, keyTable: LandscapeKeyTable, x: Int, y: Int, id: Int) {
        var buffer = cache.store.read(5, id)
        val key = keyTable.getKeys(x, y)
        buffer = Container.decode(buffer, key).getData()
        Landscape.decode(listeners, x, y, buffer)
    }

    @Throws(IOException::class)
    private fun readMap2(cache: Cache, x: Int, y: Int, id: Int) {
//        val buffer = cache.read(5, id).getData()
//        Map.decode(listeners, x, y, buffer)
//        println("Map:") //ape toll
//        println(map.getTile(2801, 2704, 0).flags)
    }

    private fun readLandscape(cache: Cache, keyTable: LandscapeKeyTable, x: Int, y: Int, fileId: Int) {
        var buffer: ByteBuffer = cache.store.read(5, fileId)
        val keys: IntArray = keyTable.getKeys(x, y)
        buffer = Container.decode(buffer, keys).getData()

        var id = -1
        while (true) {
            val deltaId = ByteBufferUtils.getSmart(buffer)
            if (deltaId == 0) {
                break
            }
            id += deltaId

            var pos = 0
            while (true) {
                val deltaPos = ByteBufferUtils.getSmart(buffer)
                if (deltaPos == 0) {
                    break
                }
                pos += deltaPos - 1

                val localX = (pos shr 6) and 0x3F
                val localY = pos and 0x3F
                val height = (pos shr 12) and 0x3

                val temp = buffer.get().toInt() and 0xFF
                val type = temp shr 2
                val rotation = temp and 0x3

                val position = Position(x * 64 + localX, y * 64 + localY, height)
                for (listener in listeners) {
                    listener.objectDecoded(id, rotation, ObjectType.forId(type), position)
                }
            }
        }
    }

    private fun readMap(cache: Cache, x: Int, y: Int, id: Int) {
        val buffer = cache.read(5, id).getData()

        for (plane in 0..3) {
            for (localX in 0..63) {
                for (localY in 0..63) {
                    val position = Position(x * 64 + localX, y * 64 + localY, plane)
                    var flags = 0

                    while (true) {
                        val config = buffer.get().toInt() and 0xFF

                        if (config == 0) {
                            for (listener in listeners) {
                                listener.tileDecoded(flags, position)
                            }
                            break
                        } else if (config == 1) {
                            buffer.get()

                            for (listener in listeners) {
                                listener.tileDecoded(flags, position)
                            }
                            break
                        } else if (config <= 49) {
                            buffer.get()
                        } else if (config <= 81) {
                            flags = config - 49
                        }
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun readNpc(cache: Cache, x: Int, y: Int, fileId: Int) {
        val buffer: ByteBuffer = cache.read(5, fileId).getData()
        while (buffer.hasRemaining()) {
            val compressedData = buffer.getShort().toInt() and 0xFFFFF
            val height = compressedData shr 14
            val localX = 63 and (compressedData shr 7)
            val localY = compressedData and 63
            val npcId = buffer.getShort().toInt()

//            val npc: net.scapeemulator.game.model.npc.NPC = NormalNPC(npcId)
//            npc.setPosition(Position((x * 64) + localX, (y * 64) + localY, height))
//            World.getWorld().addNpc(npc)
        }
    }
}