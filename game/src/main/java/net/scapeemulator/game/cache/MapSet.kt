package net.scapeemulator.game.cache

import net.scapeemulator.cache.Cache
import net.scapeemulator.cache.Container
import net.scapeemulator.cache.Container.Companion.decode
import net.scapeemulator.cache.ReferenceTable
import net.scapeemulator.cache.util.StringUtils
import net.scapeemulator.game.util.LandscapeKeyTable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

object MapSet {
    private val logger: Logger = LoggerFactory.getLogger(MapSet::class.java)

    @Throws(IOException::class)
    fun init(cache: Cache, keyTable: LandscapeKeyTable) {
        logger.info("Reading map and landscape files...")

        val rt = ReferenceTable.decode(Container.decode(cache.store.read(255, 5)).getData())

        for (x in 0..255) {
            for (y in 0..255) {
                val landscapeFile = "l" + x + "_" + y
                val mapFile = "m" + x + "_" + y

                val landscapeIdentifier = StringUtils.hash(landscapeFile)
                val mapIdentifier = StringUtils.hash(mapFile)

                for (id in 0..<rt.capacity()) {
                    val entry = rt.getEntry(id)
                    if (entry == null) continue

                    try {
                        if (entry.getIdentifier() == landscapeIdentifier) readLandscape(cache, keyTable, x, y, id)
                        else if (entry.getIdentifier() == mapIdentifier) readMap(cache, x, y, id)
                    } catch (ex: Exception) {
                        logger.debug("Failed to read map/landscape file " + x + ", " + y + ".", ex)
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun readLandscape(cache: Cache, keyTable: LandscapeKeyTable, x: Int, y: Int, id: Int) {
        var buffer = cache.store.read(5, id)

        val key = keyTable.getKeys(x, y)
        buffer = decode(buffer, key).getData()

        val landscape = Landscape.decode(x, y, buffer)
    }

    @Throws(IOException::class)
    private fun readMap(cache: Cache, x: Int, y: Int, id: Int) {
        val buffer = cache.read(5, id).getData()
        val map = Map.decode(x, y, buffer)
    }
}
