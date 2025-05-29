package net.scapeemulator.cache.tools

import net.scapeemulator.cache.Container.Companion.decode
import net.scapeemulator.cache.FileStore
import net.scapeemulator.cache.FileStore.Companion.open
import net.scapeemulator.cache.ReferenceTable
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.CRC32

object CacheAggregator {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val otherStore = open("/home/graham/Downloads/rscd/data/")
        val store = open("../game/data/cache/")

        for (type in 0 until store.getFileCount(255)) {
            if (type == 7) continue  // TODO need support for newer ref table format for this index


            val otherTable = ReferenceTable.decode(decode(otherStore.read(255, type)).getData())
            val table = ReferenceTable.decode(decode(store.read(255, type)).getData())
            for (file in 0 until table.capacity()) {
                val entry = table.getEntry(file) ?: continue

                if (isRepackingRequired(store, entry, type, file)) {
                    val otherEntry = otherTable.getEntry(file)
                    if (entry.version == otherEntry!!.version && entry.crc == otherEntry.crc) {
                        store.write(type, file, otherStore.read(type, file))
                    }
                }
            }
        }
    }

    private fun isRepackingRequired(store: FileStore, entry: ReferenceTable.Entry, type: Int, file: Int): Boolean {
        val buffer: ByteBuffer
        try {
            buffer = store.read(type, file)
        } catch (ex: IOException) {
            return true
        }

        if (buffer.capacity() <= 2) {
            return true
        }

        val bytes = ByteArray(buffer.limit() - 2) // last two bytes are the version and shouldn't be included
        buffer.position(0)
        buffer[bytes, 0, bytes.size]

        val crc = CRC32()
        crc.update(bytes, 0, bytes.size)

        if (crc.value.toInt() != entry.crc) {
            return true
        }

        buffer.position(buffer.limit() - 2)
        if ((buffer.getShort().toInt() and 0xFFFF) != entry.version) {
            return true
        }

        return false
    }
}
