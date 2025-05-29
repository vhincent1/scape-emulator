package net.scapeemulator.cache.tools

import net.scapeemulator.cache.Container.Companion.decode
import net.scapeemulator.cache.FileStore.Companion.open
import net.scapeemulator.cache.ReferenceTable
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.CRC32

object CacheVerifier {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        open("../game/data/cache/").use { store ->
            for (type in 0 until store.getFileCount(255)) {
                val table = ReferenceTable.decode(decode(store.read(255, type)).getData())
                for (file in 0 until table.capacity()) {
                    val entry = table.getEntry(file) ?: continue

                    var buffer: ByteBuffer
                    try {
                        buffer = store.read(type, file)
                    } catch (ex: IOException) {
                        println("$type:$file error")
                        continue
                    }

                    if (buffer.capacity() <= 2) {
                        println("$type:$file missing")
                        continue
                    }

                    val bytes =
                        ByteArray(buffer.limit() - 2) // last two bytes are the version and shouldn't be included
                    buffer.position(0)
                    buffer[bytes, 0, bytes.size]

                    val crc = CRC32()
                    crc.update(bytes, 0, bytes.size)

                    if (crc.value.toInt() != entry.crc) {
                        println("$type:$file corrupt")
                    }

                    buffer.position(buffer.limit() - 2)
                    if ((buffer.getShort().toInt() and 0xFFFF) != entry.version) {
                        println("$type:$file out of date")
                    }
                }
            }
        }
    }
}
