package net.scapeemulator.cache.util.crypto

import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * An implementation of the RSA algorithm.
 * @author Graham
 * @author `Discardedx2
 */
object Rsa {
    /**
     * Encrypts/decrypts the specified buffer with the key and modulus.
     * @param buffer The input buffer.
     * @param modulus The modulus.
     * @param key The key.
     * @return The output buffer.
     */
    @JvmStatic
    fun crypt(buffer: ByteBuffer, modulus: BigInteger, key: BigInteger): ByteBuffer {
        val bytes = ByteArray(buffer.limit())
        buffer.get(bytes)

        val `in` = BigInteger(bytes)
        val out = `in`.modPow(key, modulus)

        return ByteBuffer.wrap(out.toByteArray())
    }
}
