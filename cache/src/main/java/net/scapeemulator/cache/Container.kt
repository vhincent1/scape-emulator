package net.scapeemulator.cache

import net.scapeemulator.cache.util.CompressionUtils
import net.scapeemulator.cache.util.crypto.Xtea
import java.io.IOException
import java.nio.ByteBuffer

/**
 * A [Container] holds an optionally compressed file. This class can be
 * used to decompress and compress containers. A container can also have a two
 * byte trailer which specifies the version of the file within it.
 * @author Graham
 * @author `Discardedx2
 */
class Container
/**
 * Creates a new unversioned container.
 * @param type The type of compression.
 * @param data The decompressed data.
 */ @JvmOverloads constructor(
	/**
     * The type of compression this container uses.
     */
	@JvmField var type: Int,
	/**
     * The decompressed data.
     */
    private val data: ByteBuffer,
	/**
     * The version of the file within this container.
     */
    private var version: Int = -1
) {
    /**
     * Gets the type of this container.
     * @return The compression type.
     */
    /**
     * Sets the type of this container.
     * @param type The compression type.
     */

    /**
     * Creates a new versioned container.
     * @param type The type of compression.
     * @param data The decompressed data.
     * @param version The version of the file within this container.
     */

    val isVersioned: Boolean
        /**
         * Checks if this container is versioned.
         * @return `true` if so, `false` if not.
         */
        get() = version != -1

    /**
     * Gets the version of the file in this container.
     * @return The version of the file.
     * @throws IllegalArgumentException if this container is not versioned.
     */
    fun getVersion(): Int {
        check(this.isVersioned)

        return version
    }

    /**
     * Sets the version of this container.
     * @param version The version.
     */
    fun setVersion(version: Int) {
        this.version = version
    }

    /**
     * Removes the version on this container so it becomes unversioned.
     */
    fun removeVersion() {
        this.version = -1
    }

    /**
     * Gets the decompressed data.
     * @return The decompressed data.
     */
    fun getData(): ByteBuffer {
        return data.asReadOnlyBuffer()
    }

    /**
     * Encodes and compresses this container.
     * @return The buffer.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun encode(): ByteBuffer {
        val data = getData() // so we have a read only view, making this method thread safe

        /* grab the data as a byte array for compression */
        val bytes = ByteArray(data.limit())
        data.mark()
        data.get(bytes)
        data.reset()

        /* compress the data */
        val compressed: ByteArray
        if (type == COMPRESSION_NONE) {
            compressed = bytes
        } else if (type == COMPRESSION_GZIP) {
            compressed = CompressionUtils.gzip(bytes)
        } else if (type == COMPRESSION_BZIP2) {
            compressed = CompressionUtils.bzip2(bytes)
        } else {
            throw IOException("Invalid compression type")
        }

        /* calculate the size of the header and trailer and allocate a buffer */
        val header = 5 + (if (type == COMPRESSION_NONE) 0 else 4) + (if (this.isVersioned) 2 else 0)
        val buf = ByteBuffer.allocate(header + compressed.size)

        /* write the header, with the optional uncompressed length */
        buf.put(type.toByte())
        buf.putInt(compressed.size)

        /* write the compressed length */
        if (type != COMPRESSION_NONE) {
            buf.putInt(data.limit())
        }

        /* write the compressed data */
        buf.put(compressed)

        /* write the trailer with the optional version */
        if (this.isVersioned) {
            buf.putShort(version.toShort())
        }

        /* flip the buffer and return it */
        return buf.flip() as ByteBuffer
    }

    companion object {
        /**
         * This type indicates that no compression is used.
         */
        const val COMPRESSION_NONE: Int = 0

        /**
         * This type indicates that BZIP2 compression is used.
         */
        const val COMPRESSION_BZIP2: Int = 1

        /**
         * This type indicates that GZIP compression is used.
         */
        const val COMPRESSION_GZIP: Int = 2

        private val NULL_KEY = IntArray(4)

        /**
         * Decodes and decompresses the container.
         * @param buffer The buffer.
         * @return The decompressed container.
         * @throws IOException if an I/O error occurs.
         */
        @JvmStatic
		@Throws(IOException::class)
        fun decode(buffer: ByteBuffer): Container {
            return decode(buffer, NULL_KEY)
        }

        @Throws(IOException::class)
        fun decode(buffer: ByteBuffer, key: IntArray): Container {
            /* decode the type and length */
            val type = buffer.get().toInt() and 0xFF
            val length = buffer.getInt()

            /* decrypt (TODO what to do about version number trailer?) */
            if (key[0] != 0 || key[1] != 0 || key[2] != 0 || key[3] != 0) {
                Xtea.decipher(buffer, 5, length + (if (type == COMPRESSION_NONE) 5 else 9), key)
            }

            /* check if we should decompress the data or not */
            if (type == COMPRESSION_NONE) {
                /* simply grab the data and wrap it in a buffer */
                val temp = ByteArray(length)
                buffer.get(temp)
                val data = ByteBuffer.wrap(temp)

                /* decode the version if present */
                var version = -1
                if (buffer.remaining() >= 2) {
                    version = buffer.getShort().toInt()
                }

                /* and return the decoded container */
                return Container(type, data, version)
            } else {
                /* grab the length of the uncompressed data */
                val uncompressedLength = buffer.getInt()

                /* grab the data */
                val compressed = ByteArray(length)
                buffer.get(compressed)

                /* uncompress it */
                val uncompressed: ByteArray
                if (type == COMPRESSION_BZIP2) {
                    uncompressed = CompressionUtils.bunzip2(compressed)
                } else if (type == COMPRESSION_GZIP) {
                    uncompressed = CompressionUtils.gunzip(compressed)
                } else {
                    throw IOException("Invalid compression type")
                }

                /* check if the lengths are equal */
                if (uncompressed.size != uncompressedLength) {
                    throw IOException("Length mismatch")
                }

                /* decode the version if present */
                var version = -1
                if (buffer.remaining() >= 2) {
                    version = buffer.getShort().toInt()
                }

                /* and return the decoded container */
                return Container(type, ByteBuffer.wrap(uncompressed), version)
            }
        }
    }
}
