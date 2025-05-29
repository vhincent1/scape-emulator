package net.scapeemulator.cache.tools

import net.scapeemulator.cache.Container.Companion.decode
import net.scapeemulator.cache.FileStore.Companion.create
import net.scapeemulator.cache.FileStore.Companion.open
import net.scapeemulator.cache.ReferenceTable
import java.io.IOException

object CacheDefragmenter {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        open("../game/data/cache/").use { `in` ->
            create("/tmp/defragmented-cache", `in`.typeCount).use { out ->
                for (type in 0 until `in`.typeCount) {
                    val buf = `in`.read(255, type)
                    buf.mark()
                    out.write(255, type, buf)
                    buf.reset()

                    val rt = ReferenceTable.decode(decode(buf).getData())
                    for (file in 0 until rt.capacity()) {
                        if (rt.getEntry(file) == null) continue

                        out.write(type, file, `in`.read(type, file))
                    }
                }
            }
        }
    }
}
