package net.scapeemulator.cache

import net.scapeemulator.cache.util.ByteBufferUtils
import java.nio.ByteBuffer

/**
 * An [Index] points to a file inside a [FileStore].
 * @author Graham
 * @author `Discardedx2
 */
class Index
/**
 * Creates a new index.
 * @param size The size of the file in bytes.
 * @param sector The number of the first sector that contains the file.
 */(
    /**
     * The size of the file in bytes.
     */
    val size: Int,
    /**
     * The number of the first sector that contains the file.
     */
    val sector: Int
) {
    /**
     * Gets the size of the file.
     * @return The size of the file in bytes.
     */

    /**
     * Gets the number of the first sector that contains the file.
     * @return The number of the first sector that contains the file.
     */

    /**
     * Encodes this index into a byte buffer.
     * @return The buffer.
     */
    fun encode(): ByteBuffer {
        val buf = ByteBuffer.allocate(SIZE)
        ByteBufferUtils.putTriByte(buf, size)
        ByteBufferUtils.putTriByte(buf, sector)
        return buf.flip() as ByteBuffer
    }

    companion object {
        /**
         * The size of an index, in bytes.
         */
        const val SIZE: Int = 6

        /**
         * Decodes the specified [ByteBuffer] into an [Index] object.
         * @param buf The buffer.
         * @return The index.
         */
        fun decode(buf: ByteBuffer): Index {
            require(buf.remaining() == SIZE)

            val size = ByteBufferUtils.getTriByte(buf)
            val sector = ByteBufferUtils.getTriByte(buf)
            return Index(size, sector)
        }
    }
}