package net.scapeemulator.cache.util.crypto

import java.nio.ByteBuffer

/**
 * An implementation of the XTEA block cipher.
 * @author Graham
 * @author `Discardedx2
 */
object Xtea {
    /**
     * The golden ratio.
     */
    const val GOLDEN_RATIO: Int = -0x61c88647

    /**
     * The number of rounds.
     */
    const val ROUNDS: Int = 32

    /**
     * Deciphers the specified [ByteBuffer] with the given key.
     * @param buffer The buffer.
     * @param key The key.
     * @throws IllegalArgumentException if the key is not exactly 4 elements
     * long.
     */
	@JvmStatic
	fun decipher(buffer: ByteBuffer, start: Int, end: Int, key: IntArray) {
        require(key.size == 4)

        val numQuads = (end - start) / 8
        for (i in 0..<numQuads) {
            var sum = GOLDEN_RATIO * ROUNDS
            var v0 = buffer.getInt(start + i * 8)
            var v1 = buffer.getInt(start + i * 8 + 4)
            for (j in 0..<ROUNDS) {
                v1 -= (((v0 shl 4) xor (v0 ushr 5)) + v0) xor (sum + key[(sum ushr 11) and 3])
                sum -= GOLDEN_RATIO
                v0 -= (((v1 shl 4) xor (v1 ushr 5)) + v1) xor (sum + key[sum and 3])
            }
            buffer.putInt(start + i * 8, v0)
            buffer.putInt(start + i * 8 + 4, v1)
        }
    }

    /**
     * Enciphers the specified [ByteBuffer] with the given key.
     * @param buffer The buffer.
     * @param key The key.
     * @throws IllegalArgumentException if the key is not exactly 4 elements
     * long.
     */
	@JvmStatic
	fun encipher(buffer: ByteBuffer, start: Int, end: Int, key: IntArray) {
        require(key.size == 4)

        val numQuads = (end - start) / 8
        for (i in 0..<numQuads) {
            var sum = 0
            var v0 = buffer.getInt(start + i * 8)
            var v1 = buffer.getInt(start + i * 8 + 4)
            for (j in 0..<ROUNDS) {
                v0 += (((v1 shl 4) xor (v1 ushr 5)) + v1) xor (sum + key[sum and 3])
                sum += GOLDEN_RATIO
                v1 += (((v0 shl 4) xor (v0 ushr 5)) + v0) xor (sum + key[(sum ushr 11) and 3])
            }
            buffer.putInt(start + i * 8, v0)
            buffer.putInt(start + i * 8 + 4, v1)
        }
    }
}
