package net.scapeemulator.cache

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer

/**
 * An [Archive] is a file within the cache that can have multiple member
 * files inside it.
 * @author Graham
 * @author `Discardedx2
 */
class Archive(size: Int) {
    /**
     * The array of entries in this archive.
     */
    private val entries: Array<ByteBuffer>

    /**
     * Creates a new archive.
     * @param size The number of entries in the archive.
     */
    init {
        @Suppress("UNCHECKED_CAST")
        this.entries = arrayOfNulls<ByteBuffer>(size) as Array<ByteBuffer>
    }

    /**
     * Encodes this [Archive] into a [ByteBuffer].
     *
     *
     * Please note that this is a fairly simple implementation that does not
     * attempt to use more than one chunk.
     * @return An encoded [ByteBuffer].
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun encode(): ByteBuffer { // TODO: an implementation that can use more than one chunk
        val bout = ByteArrayOutputStream()
        DataOutputStream(bout).use { os ->
            /* add the data for each entry */
            for (entry in entries) {
                /* copy to temp buffer */
                val temp = ByteArray(entry.limit())
                entry.position(0)
                entry.get(temp)
                entry.position(0)

                /* copy to output stream */
                os.write(temp)
            }

            /* write the chunk lengths */
            var prev = 0
            for (entry in entries) {
                /*
				 * since each file is stored in the only chunk, just write the
				 * delta-encoded file size
				 */
                val chunkSize = entry.limit()
                os.writeInt(chunkSize - prev)
                prev = chunkSize
            }

            /* we only used one chunk due to a limitation of the implementation */
            bout.write(1)

            /* wrap the bytes from the stream in a buffer */
            val bytes = bout.toByteArray()
            return ByteBuffer.wrap(bytes)
        }
    }

    /**
     * Gets the size of this archive.
     * @return The size of this archive.
     */
    fun size(): Int {
        return entries.size
    }

    /**
     * Gets the entry with the specified id.
     * @param id The id.
     * @return The entry.
     */
    fun getEntry(id: Int): ByteBuffer? {
        return entries[id]
    }

    /**
     * Inserts/replaces the entry with the specified id.
     * @param id The id.
     * @param buffer The entry.
     */
    fun putEntry(id: Int, buffer: ByteBuffer?) {
        entries[id] = buffer!!
    }

    companion object {
        /**
         * Decodes the specified [ByteBuffer] into an [Archive].
         * @param buffer The buffer.
         * @param size The size of the archive.
         * @return The decoded [Archive].
         */
        fun decode(buffer: ByteBuffer, size: Int): Archive {
            /* allocate a new archive object */
            val archive = Archive(size)

            /* read the number of chunks at the end of the archive */
            buffer.position(buffer.limit() - 1)
            val chunks = buffer.get().toInt() and 0xFF

            /* read the sizes of the child entries and individual chunks */
            val chunkSizes = Array<IntArray?>(chunks) { IntArray(size) }
            val sizes = IntArray(size)
            buffer.position(buffer.limit() - 1 - chunks * size * 4)
            for (chunk in 0..<chunks) {
                var chunkSize = 0
                for (id in 0..<size) {
                    /* read the delta-encoded chunk length */
                    val delta = buffer.getInt()
                    chunkSize += delta

                    chunkSizes[chunk]!![id] = chunkSize /* store the size of this chunk */
                    sizes[id] += chunkSize /* and add it to the size of the whole file */
                }
            }

            /* allocate the buffers for the child entries */
            for (id in 0..<size) {
                archive.entries[id] = ByteBuffer.allocate(sizes[id])
            }

            /* read the data into the buffers */
            buffer.position(0)
            for (chunk in 0..<chunks) {
                for (id in 0..<size) {
                    /* get the length of this chunk */
                    val chunkSize = chunkSizes[chunk]!![id]

                    /* copy this chunk into a temporary buffer */
                    val temp = ByteArray(chunkSize)
                    buffer.get(temp)

                    /* copy the temporary buffer into the file buffer */
                    archive.entries[id].put(temp)
                }
            }

            /* flip all of the buffers */
            for (id in 0..<size) {
                archive.entries[id].flip()
            }

            /* return the archive */
            return archive
        }
    }
}
