package net.scapeemulator.cache.util

object StringUtils {
    fun hash(str: String): Int {
        var hash = 0
        for (i in 0..<str.length) {
            hash = str.get(i).code + ((hash shl 5) - hash)
        }
        return hash
    }
}
