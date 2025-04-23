package net.scapeemulator.cache

import net.scapeemulator.cache.util.FileChannelUtils
import java.io.Closeable
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption

/**
 * A file store holds multiple files inside a "virtual" file system made up of
 * several index files and a single data file.
 * @author Graham
 * @author `Discardedx2
 */
class FileStore
/**
 * Creates a new file store.
 * @param dataChannel The data file.
 * @param indexChannels The index files.
 * @param metaChannel The 'meta' index file.
 */(
    /**
     * The data file.
     */
    private val dataChannel: FileChannel,
    /**
     * The index files.
     */
    private val indexChannels: Array<FileChannel>,
    /**
     * The 'meta' index files.
     */
    private val metaChannel: FileChannel
) : Closeable {
    @get:Throws(IOException::class)
    val typeCount: Int
        /**
         * Gets the number of index files, not including the meta index file.
         * @return The number of index files.
         * @throws IOException if an I/O error occurs.
         */
        get() = indexChannels.size

    /**
     * Gets the number of files of the specified type.
     * @param type The type.
     * @return The number of files.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun getFileCount(type: Int): Int {
        if ((type < 0 || type >= indexChannels.size) && type != 255) throw FileNotFoundException()

        if (type == 255) return (metaChannel.size() / Index.SIZE).toInt()
        return (indexChannels[type].size() / Index.SIZE).toInt()
    }

    /**
     * Writes a file.
     * @param type The type of the file.
     * @param id The id of the file.
     * @param data A [ByteBuffer] containing the contents of the file.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun write(type: Int, id: Int, data: ByteBuffer) {
        data.mark()
        if (!write(type, id, data, true)) {
            data.reset()
            write(type, id, data, false)
        }
    }

    /**
     * Writes a file.
     * @param type The type of the file.
     * @param id The id of the file.
     * @param data A [ByteBuffer] containing the contents of the file.
     * @param overwrite A flag indicating if the existing file should be
     * overwritten.
     * @return A flag indicating if the file was written successfully.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    private fun write(type: Int, id: Int, data: ByteBuffer, overwrite: Boolean): Boolean {
        var overwrite = overwrite
        if ((type < 0 || type >= indexChannels.size) && type != 255) throw FileNotFoundException()

        val indexChannel = if (type == 255) metaChannel else indexChannels[type]

        var nextSector: Int
        var ptr = (id * Index.SIZE).toLong()
        if (overwrite) {
            if (ptr < 0) throw IOException()
            else if (ptr >= indexChannel.size()) return false

            val buf = ByteBuffer.allocate(Index.SIZE)
            FileChannelUtils.readFully(indexChannel, buf, ptr)

            val index = Index.decode(buf.flip() as ByteBuffer)
            nextSector = index.sector
            if (nextSector <= 0 || nextSector > dataChannel.size() * Sector.SIZE) return false
        } else {
            nextSector = ((dataChannel.size() + Sector.SIZE - 1) / Sector.SIZE).toInt()
            if (nextSector == 0) nextSector = 1
        }

        val index = Index(data.remaining(), nextSector)
        indexChannel.write(index.encode(), ptr)

        val buf = ByteBuffer.allocate(Sector.SIZE)

        var chunk = 0
        var remaining = index.size
        do {
            val curSector = nextSector
            ptr = (curSector * Sector.SIZE).toLong()
            nextSector = 0

            if (overwrite) {
                buf.clear()
                FileChannelUtils.readFully(dataChannel, buf, ptr)

                val sector = Sector.decode(buf.flip() as ByteBuffer)

                if (sector.type != type) return false

                if (sector.id != id) return false

                if (sector.chunk != chunk) return false

                nextSector = sector.nextSector
                if (nextSector < 0 || nextSector > dataChannel.size() / Sector.SIZE) return false
            }

            if (nextSector == 0) {
                overwrite = false
                nextSector = ((dataChannel.size() + Sector.SIZE - 1) / Sector.SIZE).toInt()
                if (nextSector == 0) nextSector++
                if (nextSector == curSector) nextSector++
            }

            val bytes = ByteArray(Sector.DATA_SIZE)
            if (remaining < Sector.DATA_SIZE) {
                data.get(bytes, 0, remaining)
                nextSector = 0 // mark as EOF
                remaining = 0
            } else {
                remaining -= Sector.DATA_SIZE
                data.get(bytes, 0, Sector.DATA_SIZE)
            }

            val sector = Sector(type, id, chunk++, nextSector, bytes)
            dataChannel.write(sector.encode(), ptr)
        } while (remaining > 0)

        return true
    }

    /**
     * Reads a file.
     * @param type The type of the file.
     * @param id The id of the file.
     * @return A [ByteBuffer] containing the contents of the file.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun read(type: Int, id: Int): ByteBuffer {
        if ((type < 0 || type >= indexChannels.size) && type != 255) throw FileNotFoundException()

        val indexChannel = if (type == 255) metaChannel else indexChannels[type]

        var ptr = (id * Index.SIZE).toLong()
        if (ptr < 0 || ptr >= indexChannel.size()) throw FileNotFoundException()

        var buf = ByteBuffer.allocate(Index.SIZE)
        FileChannelUtils.readFully(indexChannel, buf, ptr)

        val index = Index.decode(buf.flip() as ByteBuffer)

        val data = ByteBuffer.allocate(index.size)
        buf = ByteBuffer.allocate(Sector.SIZE)

        var chunk = 0
        var remaining = index.size
        ptr = (index.sector * Sector.SIZE).toLong()
        do {
            buf.clear()
            FileChannelUtils.readFully(dataChannel, buf, ptr)
            val sector = Sector.decode(buf.flip() as ByteBuffer)

            if (remaining > Sector.DATA_SIZE) {
                data.put(sector.data, 0, Sector.DATA_SIZE)
                remaining -= Sector.DATA_SIZE

                if (sector.type != type) throw IOException("File type mismatch.")

                if (sector.id != id) throw IOException("File id mismatch.")

                if (sector.chunk != chunk++) throw IOException("Chunk mismatch.")

                ptr = (sector.nextSector * Sector.SIZE).toLong()
            } else {
                data.put(sector.data, 0, remaining)
                remaining = 0
            }
        } while (remaining > 0)

        return data.flip() as ByteBuffer
    }

    @Throws(IOException::class)
    override fun close() {
        dataChannel.close()

        for (channel in indexChannels) channel.close()

        metaChannel.close()
    }

    companion object {
        @JvmStatic
		@Throws(IOException::class)
        fun create(root: String, indexes: Int): FileStore {
            return create(File(root), indexes)
        }

        @Throws(IOException::class)
        fun create(root: File, indexes: Int): FileStore {
            if (!root.mkdirs()) throw IOException()

            for (i in 0..<indexes) {
                val index = File(root, "main_file_cache.idx" + i)
                if (!index.createNewFile()) throw IOException()
            }

            val meta = File(root, "main_file_cache.idx255")
            if (!meta.createNewFile()) throw IOException()

            val data = File(root, "main_file_cache.dat2")
            if (!data.createNewFile()) throw IOException()

            return open(root)
        }

        /**
         * Opens the file store stored in the specified directory.
         * @param root The directory containing the index and data files.
         * @return The file store.
         * @throws IOException if any of the `main_file_cache.*` files could
         * not be opened.
         */
        @JvmStatic
		@Throws(IOException::class)
        fun open(root: String): FileStore {
            return open(File(root))
        }

        /**
         * Opens the file store stored in the specified directory.
         * @param root The directory containing the index and data files.
         * @return The file store.
         * @throws IOException if any of the `main_file_cache.*` files could
         * not be opened.
         */
        @Throws(IOException::class)
        fun open(root: File?): FileStore {
            val data = File(root, "main_file_cache.dat2")
            if (!data.exists()) throw FileNotFoundException()

            val dataChannel = FileChannel.open(data.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE)

            val indexChannels: MutableList<FileChannel> = ArrayList<FileChannel>()
            for (i in 0..253) {
                val index = File(root, "main_file_cache.idx" + i)
                if (!index.exists()) break

                val indexChannel = FileChannel.open(index.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE)
                indexChannels.add(indexChannel)
            }

            if (indexChannels.isEmpty()) throw FileNotFoundException()

            val meta = File(root, "main_file_cache.idx255")
            if (!meta.exists()) throw FileNotFoundException()

            val metaChannel = FileChannel.open(meta.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE)

            return FileStore(dataChannel, indexChannels.toTypedArray<FileChannel>(), metaChannel)
        }
    }
}
