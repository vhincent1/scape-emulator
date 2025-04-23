package net.scapeemulator.game.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class LandscapeKeyTable {
    private val keys: MutableMap<Int, IntArray> = HashMap()

    fun getKeys(x: Int, y: Int): IntArray {
        var k = keys[(x shl 8) or y]
        if (k == null) k = EMPTY_KEY_ARRAY
        return k
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LandscapeKeyTable::class.java)
        private val EMPTY_KEY_ARRAY = IntArray(4)

        @Throws(IOException::class)
        fun open(dir: String): LandscapeKeyTable {
            return open(File(dir))
        }

        @Throws(IOException::class)
        fun open(dir: File): LandscapeKeyTable {
            val table = LandscapeKeyTable()
            for (f in dir.listFiles()) {
                val name = f.name
                if (name.matches("^[0-9]+\\.txt$".toRegex())) {
                    val region = name.substring(0, name.length - 4).toInt()
                    table.keys[region] = readKeys(f)
                }
            }
            logger.info("Loaded " + table.keys.size + " landscape keys.")
            return table
        }

        @Throws(IOException::class)
        private fun readKeys(f: File): IntArray {
            BufferedReader(FileReader(f)).use { reader ->
                val keys = IntArray(4)
                for (i in keys.indices) {
                    keys[i] = reader.readLine().toInt()
                }
                return keys
            }
        }
    }
}
