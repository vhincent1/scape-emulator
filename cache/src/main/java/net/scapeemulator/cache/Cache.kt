package net.scapeemulator.cache

import net.scapeemulator.cache.ReferenceTable.ChildEntry
import net.scapeemulator.cache.util.ByteBufferUtils.getCrcChecksum
import net.scapeemulator.cache.util.ByteBufferUtils.getWhirlpoolDigest
import net.scapeemulator.cache.util.crypto.Whirlpool
import java.io.Closeable
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.CRC32

/**
 * The [Cache] class provides a unified, high-level API for modifying
 * the cache of a Jagex game.
 * @author Graham
 * @author `Discardedx2
 */
class Cache
/**
 * Creates a new [Cache] backed by the specified [FileStore].
 * @param store The [FileStore] that backs this [Cache].
 */(
    /**
     * The file store that backs this cache.
     */
	@JvmField val store: FileStore
) : Closeable {
    /**
     * Gets the [FileStore] that backs this [Cache].
     * @return The underlying file store.
     */

    @get:Throws(IOException::class)
    val typeCount: Int
        /**
         * Gets the number of index files, not including the meta index file.
         * @return The number of index files.
         * @throws IOException if an I/O error occurs.
         */
        get() = store.typeCount

    /**
     * Gets the number of files of the specified type.
     * @param type The type.
     * @return The number of files.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun getFileCount(type: Int): Int {
        return store.getFileCount(type)
    }

    /**
     * Computes the [ChecksumTable] for this cache. The checksum table
     * forms part of the so-called "update keys".
     * @return The [ChecksumTable].
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun createChecksumTable(): ChecksumTable {
        /* create the checksum table */
        val size = store.typeCount
        val table = ChecksumTable(size)

        /* loop through all the reference tables and get their CRC and versions */
        for (i in 0..<size) {
            val buf = store.read(255, i)

            var crc = 0
            var version = 0
            var whirlpool = ByteArray(64)

            /*
			 * if there is actually a reference table, calculate the CRC,
			 * version and whirlpool hash
			 */
            if (buf.limit() > 0) { // some indices are not used, is this appropriate?
                val ref = ReferenceTable.decode(Container.decode(buf).getData())
                crc = getCrcChecksum(buf)
                version = ref.getVersion()
                buf.position(0)
                whirlpool = getWhirlpoolDigest(buf)
            }

            table.setEntry(i, ChecksumTable.Entry(crc, version, whirlpool))
        }

        /* return the table */
        return table
    }

    /**
     * Reads a file from the cache.
     * @param type The type of file.
     * @param file The file id.
     * @return The file.
     * @throws IOException if an I/O error occurred.
     */
    @Throws(IOException::class)
    fun read(type: Int, file: Int): Container {
        /* we don't want people reading/manipulating these manually */
        if (type == 255) throw IOException("Reference tables can only be read with the low level FileStore API!")

        /* delegate the call to the file store then decode the container */
        return Container.decode(store.read(type, file))
    }

    /**
     * Writes a file to the cache and updates the [ReferenceTable] that
     * it is associated with.
     * @param type The type of file.
     * @param file The file id.
     * @param container The [Container] to write.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun write(type: Int, file: Int, container: Container) {
        /* we don't want people reading/manipulating these manually */
        if (type == 255) throw IOException("Reference tables can only be modified with the low level FileStore API!")

        /* increment the container's version */
        container.setVersion(container.getVersion() + 1)

        /* decode the reference table for this index */
        var tableContainer = Container.decode(store.read(255, type))
        val table = ReferenceTable.decode(tableContainer.getData())

        /* grab the bytes we need for the checksum */
        val buffer = container.encode()
        val bytes = ByteArray(buffer.limit() - 2) // last two bytes are the version and shouldn't be included
        buffer.mark()
        try {
            buffer.position(0)
            buffer.get(bytes, 0, bytes.size)
        } finally {
            buffer.reset()
        }

        /* calculate the new CRC checksum */
        val crc = CRC32()
        crc.update(bytes, 0, bytes.size)

        /* update the version and checksum for this file */
        var entry = table.getEntry(file)
        if (entry == null) {
            /* create a new entry for the file */
            entry = ReferenceTable.Entry()
            table.putEntry(file, entry)
        }
        entry.setVersion(container.getVersion())
        entry.setCrc(crc.getValue().toInt())

        /* calculate and update the whirlpool digest if we need to */
        if ((table.getFlags() and ReferenceTable.FLAG_WHIRLPOOL) != 0) {
            val whirlpool = Whirlpool.whirlpool(bytes, 0, bytes.size)
            entry.whirlpool = whirlpool
        }

        /* update the reference table version */
        table.setVersion(table.getVersion() + 1)

        /* save the reference table */
        tableContainer = Container(tableContainer.type, table.encode())
        store.write(255, type, tableContainer.encode())

        /* save the file itself */
        store.write(type, file, buffer)
    }

    /**
     * Reads a file contained in an archive in the cache.
     * @param type The type of the file.
     * @param file The archive id.
     * @param member The file within the archive.
     * @return The file.
     * @throws IOException if an I/O error occurred.
     */
    @Throws(IOException::class)
    fun read(type: Int, file: Int, member: Int): ByteBuffer? {
        /* grab the container and the reference table */
        val container = read(type, file)
        val tableContainer = Container.decode(store.read(255, type))
        val table = ReferenceTable.decode(tableContainer.getData())

        /* check if the file/member are valid */
        val entry = table.getEntry(file)
        if (entry == null || member < 0 || member >= entry.capacity()) throw FileNotFoundException()

        /* convert member id */
        var nonSparseMember = 0
        for (i in 0..<member) {
            if (entry.getEntry(i) != null) nonSparseMember++
        }

        /* extract the entry from the archive */
        val archive = Archive.decode(container.getData(), entry.size())
        return archive.getEntry(nonSparseMember)
    }

    /**
     * Writes a file contained in an archive to the cache.
     * @param type The type of file.
     * @param file The id of the archive.
     * @param member The file within the archive.
     * @param data The data to write.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun write(type: Int, file: Int, member: Int, data: ByteBuffer?) {
        /* grab the reference table */
        var tableContainer = Container.decode(store.read(255, type))
        val table = ReferenceTable.decode(tableContainer.getData())

        /* create a new entry if necessary */
        var entry = table.getEntry(file)
        var oldArchiveSize = -1
        if (entry == null) {
            entry = ReferenceTable.Entry()
            table.putEntry(file, entry)
        } else {
            oldArchiveSize = entry.capacity()
        }

        /* add a child entry if one does not exist */
        var child = entry.getEntry(member)
        if (child == null) {
            child = ChildEntry()
            entry.putEntry(member, child)
        }

        /* extract the current archive into memory so we can modify it */
        var archive: Archive?
        val containerType: Int
        val containerVersion: Int
        if (file < store.getFileCount(type) && oldArchiveSize != -1) {
            val container = read(type, file)
            containerType = container.type
            containerVersion = container.getVersion()
            archive = Archive.decode(container.getData(), oldArchiveSize)
        } else {
            containerType = Container.COMPRESSION_GZIP
            containerVersion = 1
            archive = Archive(member + 1)
        }

        /* expand the archive if it is not large enough */
        if (member >= archive.size()) {
            val newArchive = Archive(member + 1)
            for (id in 0..<archive.size()) {
                newArchive.putEntry(id, archive.getEntry(id))
            }
            archive = newArchive
        }

        /* put the member into the archive */
        archive.putEntry(member, data)

        /* create 'dummy' entries */
        for (id in 0..<archive.size()) {
            if (archive.getEntry(id) == null) {
                entry.putEntry(id, ChildEntry())
                archive.putEntry(id, ByteBuffer.allocate(1))
            }
        }

        /* write the reference table out again */
        tableContainer = Container(tableContainer.type, table.encode())
        store.write(255, type, tableContainer.encode())

        /* and write the archive back to memory */
        val container = Container(containerType, archive.encode(), containerVersion)
        write(type, file, container)
    }

    @Throws(IOException::class)
    override fun close() {
        store.close()
    }

}
