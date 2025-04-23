package net.scapeemulator.cache

import net.scapeemulator.cache.util.ByteBufferUtils
import java.nio.ByteBuffer

/**
 * A [Sector] contains a header and data. The header contains information
 * used to verify the integrity of the cache like the current file id, type and
 * chunk. It also contains a pointer to the next sector such that the sectors
 * form a singly-linked list. The data is simply up to 512 bytes of the file.
 * @author Graham
 * @author `Discardedx2
 */
class Sector
/**
 * Creates a new sector.
 * @param type The type of the file.
 * @param id The file's id.
 * @param chunk The chunk of the file this sector contains.
 * @param nextSector The sector containing the next chunk.
 * @param data The data in this sector.
 */(
    /**
     * The type of file this sector contains.
     */
    val type: Int,
    /**
     * The id of the file this sector contains.
     */
    val id: Int,
    /**
     * The chunk within the file that this sector contains.
     */
    val chunk: Int,
    /**
     * The next sector.
     */
    val nextSector: Int,
    /**
     * The data in this sector.
     */
    val data: ByteArray
) {
    /**
     * Gets the type of file in this sector.
     * @return The type of file in this sector.
     */

    /**
     * Gets the id of the file within this sector.
     * @return The id of the file in this sector.
     */

    /**
     * Gets the chunk of the file this sector contains.
     * @return The chunk of the file this sector contains.
     */

    /**
     * Gets the next sector.
     * @return The next sector.
     */

    /**
     * Gets this sector's data.
     * @return The data within this sector.
     */

    /**
     * Encodes this sector into a [ByteBuffer].
     * @return The encoded buffer.
     */
    fun encode(): ByteBuffer {
        val buf = ByteBuffer.allocate(SIZE)

        buf.putShort(id.toShort())
        buf.putShort(chunk.toShort())
        ByteBufferUtils.putTriByte(buf, nextSector)
        buf.put(type.toByte())
        buf.put(data)

        return buf.flip() as ByteBuffer
    }

    companion object {
        /**
         * The size of the header within a sector in bytes.
         */
        const val HEADER_SIZE: Int = 8

        /**
         * The size of the data within a sector in bytes.
         */
        const val DATA_SIZE: Int = 512

        /**
         * The total size of a sector in bytes.
         */
        val SIZE: Int = HEADER_SIZE + DATA_SIZE

        /**
         * Decodes the specified [ByteBuffer] into a [Sector] object.
         * @param buf The buffer.
         * @return The sector.
         */
        fun decode(buf: ByteBuffer): Sector {
            require(buf.remaining() == SIZE)

            val id = buf.getShort().toInt() and 0xFFFF
            val chunk = buf.getShort().toInt() and 0xFFFF
            val nextSector = ByteBufferUtils.getTriByte(buf)
            val type = buf.get().toInt() and 0xFF
            val data = ByteArray(DATA_SIZE)
            buf.get(data)

            return Sector(type, id, chunk, nextSector, data)
        }
    }
}
