//package net.scapeemulator.cache
//
//import net.scapeemulator.cache.util.StringUtils
//import java.io.ByteArrayOutputStream
//import java.io.DataOutputStream
//import java.io.IOException
//import java.nio.ByteBuffer
//import java.util.*
//
///**
// * A [ReferenceTable] holds details for all the files with a single
// * type, such as checksums, versions and archive members. There are also
// * optional fields for identifier hashes and whirlpool digests.
// * @author Graham
// * @author `Discardedx2
// */
//class ReferenceTable {
//    /**
//     * The mapping for the named entries in the table.
//     */
//    /**
//     * Represents a child entry within an [Entry] in the
//     * [ReferenceTable].
//     * @author Graham Edgecombe
//     */
//    class ChildEntry {
//        /**
//         * This entry's identifier.
//         */
//        internal var identifier = -1
//
//        /**
//         * Gets the identifier of this entry.
//         * @return The identifier.
//         */
//        fun getIdentifier(): Int {
//            return identifier
//        }
//
//        /**
//         * Sets the identifier of this entry.
//         * @param identifier The identifier.
//         */
//        fun setIdentifier(identifier: Int) {
//            this.identifier = identifier
//        }
//    }
//
//    /**
//     * Represents a single entry within a [ReferenceTable].
//     * @author Graham Edgecombe
//     */
//    class Entry {
//        /**
//         * The identifier of this entry.
//         */
//        internal var identifier = -1
//
//        /**
//         * The CRC32 checksum of this entry.
//         */
//        internal var crc = 0
//
//        /**
//         * Gets the whirlpool digest of this entry.
//         * @return The whirlpool digest.
//         */
//        /**
//         * The whirlpool digest of this entry.
//         */
//        var whirlpool: ByteArray = ByteArray(64)
//            /**
//             * Sets the whirlpool digest of this entry.
//             * @param whirlpool The whirlpool digest.
//             * @throws IllegalArgumentException if the digest is not 64 bytes long.
//             */
//            set(whirlpool) {
//                require(whirlpool.size == 64)
//
//                System.arraycopy(whirlpool, 0, field, 0, whirlpool.size)
//            }
//
//        /**
//         * The version of this entry.
//         */
//        internal var version = 0
//
//        /**
//         * The children in this entry.
//         */
//        internal val entries: SortedMap<Int, ChildEntry> = TreeMap<Int, ChildEntry>()
//
//        /**
//         * Gets the identifier of this entry.
//         * @return The identifier.
//         */
//        fun getIdentifier(): Int {
//            return identifier
//        }
//
//        /**
//         * Sets the identifier of this entry.
//         * @param identifier The identifier.
//         */
//        fun setIdentifier(identifier: Int) {
//            this.identifier = identifier
//        }
//
//        /**
//         * Gets the CRC32 checksum of this entry.
//         * @return The CRC32 checksum.
//         */
//        fun getCrc(): Int {
//            return crc
//        }
//
//        /**
//         * Sets the CRC32 checksum of this entry.
//         * @param crc The CRC32 checksum.
//         */
//        fun setCrc(crc: Int) {
//            this.crc = crc
//        }
//
//        /**
//         * Gets the version of this entry.
//         * @return The version.
//         */
//        fun getVersion(): Int {
//            return version
//        }
//
//        /**
//         * Sets the version of this entry.
//         * @param version The version.
//         */
//        fun setVersion(version: Int) {
//            this.version = version
//        }
//
//        /**
//         * Gets the number of actual child entries.
//         * @return The number of actual child entries.
//         */
//        fun size(): Int {
//            return entries.size
//        }
//
//        /**
//         * Gets the maximum number of child entries.
//         * @return The maximum number of child entries.
//         */
//        fun capacity(): Int {
//            if (entries.isEmpty()) return 0
//
//            return entries.lastKey()!! + 1
//        }
//
//        /**
//         * Gets the child entry with the specified id.
//         * @param id The id.
//         * @return The entry, or `null` if it does not exist.
//         */
//        fun getEntry(id: Int): ChildEntry? {
//            return entries.get(id)
//        }
//
//        /**
//         * Replaces or inserts the child entry with the specified id.
//         * @param id The id.
//         * @param entry The entry.
//         */
//        fun putEntry(id: Int, entry: ChildEntry?) {
//            entries.put(id, entry)
//        }
//
//        /**
//         * Removes the entry with the specified id.
//         * @param id The id.
//         * @param entry The entry.
//         */
//        fun removeEntry(id: Int, entry: ChildEntry?) {
//            entries.remove(id)
//        }
//    }
//
//    /**
//     * The format of this table.
//     */
//    private var format = 0
//
//    /**
//     * The version of this table.
//     */
//    private var version = 0
//
//    /**
//     * The flags of this table.
//     */
//    private var flags = 0
//
//    /**
//     * The entries in this table.
//     */
//    private val entries: SortedMap<Int, Entry> = TreeMap<Int, Entry>()
//
//    /**
//     * Gets the format of this table.
//     * @return The format.
//     */
//    fun getFormat(): Int {
//        return format
//    }
//
//    /**
//     * Sets the format of this table.
//     * @param format The format.
//     */
//    fun setFormat(format: Int) {
//        this.format = format
//    }
//
//    /**
//     * Gets the version of this table.
//     * @return The version of this table.
//     */
//    fun getVersion(): Int {
//        return version
//    }
//
//    /**
//     * Sets the version of this table.
//     * @param version The version.
//     */
//    fun setVersion(version: Int) {
//        this.version = version
//    }
//
//    /**
//     * Gets the flags of this table.
//     * @return The flags.
//     */
//    fun getFlags(): Int {
//        return flags
//    }
//
//    /**
//     * Sets the flags of this table.
//     * @param flags The flags.
//     */
//    fun setFlags(flags: Int) {
//        this.flags = flags
//    }
//
//    /**
//     * Gets the entry with the specified id, or `null` if it does not
//     * exist.
//     * @param id The id.
//     * @return The entry.
//     */
//    fun getEntry(id: Int): Entry? {
//        return entries.get(id)
//    }
//
//    /**
//     * Gets the child entry with the specified id, or `null` if it does
//     * not exist.
//     * @param id The parent id.
//     * @param child The child id.
//     * @return The entry.
//     */
//    fun getEntry(id: Int, child: Int): ChildEntry? {
//        val entry = entries.get(id)
//        if (entry == null) return null
//
//        return entry.getEntry(child)
//    }
//
//    /**
//     * Replaces or inserts the entry with the specified id.
//     * @param id The id.
//     * @param entry The entry.
//     */
//    fun putEntry(id: Int, entry: Entry?) {
//        entries.put(id, entry)
//    }
//
//    /**
//     * Removes the entry with the specified id.
//     * @param id The id.
//     */
//    fun removeEntry(id: Int) {
//        entries.remove(id)
//    }
//
//    /**
//     * Gets the number of actual entries.
//     * @return The number of actual entries.
//     */
//    fun size(): Int {
//        return entries.size
//    }
//
//    /**
//     * Gets the maximum number of entries in this table.
//     * @return The maximum number of entries.
//     */
//    fun capacity(): Int {
//        if (entries.isEmpty()) return 0
//
//        return entries.lastKey()!! + 1
//    }
//
//    /**
//     * Encodes this [ReferenceTable] into a [ByteBuffer].
//     * @return The [ByteBuffer].
//     * @throws IOException if an I/O error occurs.
//     */
//    @Throws(IOException::class)
//    fun encode(): ByteBuffer {
//        /*
//		 * we can't (easily) predict the size ahead of time, so we write to a
//		 * stream and then to the buffer
//		 */
//        val bout = ByteArrayOutputStream()
//        DataOutputStream(bout).use { os ->
//            /* write the header */
//            os.write(format)
//            if (format >= 6) {
//                os.writeInt(version)
//            }
//            os.write(flags)
//
//            /* calculate and write the number of non-null entries */
//            os.writeShort(entries.size)
//
//            /* write the ids */
//            var last = 0
//            for (id in 0..<capacity()) {
//                if (entries.containsKey(id)) {
//                    val delta = id - last
//                    os.writeShort(delta)
//                    last = id
//                }
//            }
//
//            /* write the identifiers if required */
//            if ((flags and FLAG_IDENTIFIERS) != 0) {
//                for (entry in entries.values) {
//                    os.writeInt(entry.identifier)
//                }
//            }
//
//            /* write the CRC checksums */
//            for (entry in entries.values) {
//                os.writeInt(entry.crc)
//            }
//
//            /* write the whirlpool digests if required */
//            if ((flags and FLAG_WHIRLPOOL) != 0) {
//                for (entry in entries.values) {
//                    os.write(entry.whirlpool)
//                }
//            }
//
//            /* write the versions */
//            for (entry in entries.values) {
//                os.writeInt(entry.version)
//            }
//
//            /* calculate and write the number of non-null child entries */
//            for (entry in entries.values) {
//                os.writeShort(entry.entries.size)
//            }
//
//            /* write the child ids */
//            for (entry in entries.values) {
//                last = 0
//                for (id in 0..<entry.capacity()) {
//                    if (entry.entries.containsKey(id)) {
//                        val delta = id - last
//                        os.writeShort(delta)
//                        last = id
//                    }
//                }
//            }
//
//            /* write the child identifiers if required  */
//            if ((flags and FLAG_IDENTIFIERS) != 0) {
//                for (entry in entries.values) {
//                    for (child in entry.entries.values) {
//                        os.writeInt(child.identifier)
//                    }
//                }
//            }
//
//            /* convert the stream to a byte array and then wrap a buffer */
//            val bytes = bout.toByteArray()
//            return ByteBuffer.wrap(bytes)
//        }
//    }
//
//    companion object {
//        /**
//         * A flag which indicates this [ReferenceTable] contains
//         * [StringUtils] hashed identifiers.
//         */
//        const val FLAG_IDENTIFIERS: Int = 0x01
//
//        /**
//         * A flag which indicates this [ReferenceTable] contains
//         * whirlpool digests for its entries.
//         */
//        const val FLAG_WHIRLPOOL: Int = 0x02
//
//        /**
//         * Decodes the slave checksum table contained in the specified
//         * [ByteBuffer].
//         * @param buffer The buffer.
//         * @return The slave checksum table.
//         */
//        @JvmStatic
//        fun decode(buffer: ByteBuffer): ReferenceTable {
//            /* create a new table */
//            val table = ReferenceTable()
//
//            /* read header */
//            table.format = buffer.get().toInt() and 0xFF
//            if (table.format >= 6) {
//                table.version = buffer.getInt()
//            }
//            table.flags = buffer.get().toInt() and 0xFF
//
//            /* read the ids */
//            val ids = IntArray(buffer.getShort().toInt() and 0xFFFF)
//            var accumulator = 0
//            var size = -1
//            for (i in ids.indices) {
//                val delta = buffer.getShort().toInt() and 0xFFFF
//                accumulator += delta
//                ids[i] = accumulator
//                if (ids[i] > size) {
//                    size = ids[i]
//                }
//            }
//            size++
//
//            /* and allocate specific entries within that array */
//            for (id in ids) {
//                table.entries.put(id, Entry())
//            }
//
//            /* read the identifiers if present */
//            if ((table.flags and FLAG_IDENTIFIERS) != 0) {
//                for (id in ids) {
//                    table.entries.get(id)?.identifier = buffer.getInt()
//                }
//            }
//
//            /* read the CRC32 checksums */
//            for (id in ids) {
//                table.entries.get(id)?.crc = buffer.getInt()
//            }
//
//            /* read the whirlpool digests if present */
//            if ((table.flags and FLAG_WHIRLPOOL) != 0) {
//                for (id in ids) {
//                    buffer.get(table.entries.get(id)?.whirlpool)
//                }
//            }
//
//            /* read the version numbers */
//            for (id in ids) {
//                table.entries.get(id)?.version = buffer.getInt()
//            }
//
//            /* read the child sizes */
//            val members = arrayOfNulls<IntArray>(size)
//            for (id in ids) {
//                members[id] = IntArray(buffer.getShort().toInt() and 0xFFFF)
//            }
//
//            /* read the child ids */
//            for (id in ids) {
//                /* reset the accumulator and size */
//                accumulator = 0
//                size = -1
//
//                /* loop through the array of ids */
//                for (i in members[id]!!.indices) {
//                    val delta = buffer.getShort().toInt() and 0xFFFF
//                    accumulator += delta
//                    members[id]!![i] = accumulator
//                    if (members[id]!![i] > size) {
//                        size = members[id]!![i]
//                    }
//                }
//                size++
//
//                /* and allocate specific entries within the array */
//                for (child in members[id]!!) {
//                    table.entries.get(id)?.entries?.put(child, ChildEntry())
//                }
//            }
//
//            /* read the child identifiers if present */
//            if ((table.flags and FLAG_IDENTIFIERS) != 0) {
//                for (id in ids) {
//                    for (child in members[id]!!) {
//                        table.entries.get(id)?.entries?.get(child)?.identifier = buffer.getInt()
//                    }
//                }
//            }
//
//            /* return the table we constructed */
//            return table
//        }
//    }
//}
package net.scapeemulator.cache

import net.scapeemulator.cache.util.StringUtils
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

/**
 * A [ReferenceTable] holds details for all the files with a single
 * type, such as checksums, versions and archive members. There are also
 * optional fields for identifier hashes and whirlpool digests.
 * @author Graham
 * @author `Discardedx2
 */
class ReferenceTable {
    /**
     * Represents a child entry within an [Entry] in the
     * [ReferenceTable].
     * @author Graham Edgecombe
     */
    class ChildEntry {
        /**
         * Gets the identifier of this entry.
         * @return The identifier.
         */
        /**
         * Sets the identifier of this entry.
         * @param identifier The identifier.
         */
        /**
         * This entry's identifier.
         */
        var identifier: Int = -1
    }

    /**
     * Represents a single entry within a [ReferenceTable].
     * @author Graham Edgecombe
     */
    class Entry {
        /**
         * Gets the identifier of this entry.
         * @return The identifier.
         */
        /**
         * Sets the identifier of this entry.
         * @param identifier The identifier.
         */
        /**
         * The identifier of this entry.
         */
        var identifier: Int = -1

        /**
         * Gets the CRC32 checksum of this entry.
         * @return The CRC32 checksum.
         */
        /**
         * Sets the CRC32 checksum of this entry.
         * @param crc The CRC32 checksum.
         */
        /**
         * The CRC32 checksum of this entry.
         */
        var crc: Int = 0

        /**
         * Gets the whirlpool digest of this entry.
         * @return The whirlpool digest.
         */
        /**
         * The whirlpool digest of this entry.
         */
        var whirlpool: ByteArray = ByteArray(64)
            /**
             * Sets the whirlpool digest of this entry.
             * @param whirlpool The whirlpool digest.
             * @throws IllegalArgumentException if the digest is not 64 bytes long.
             */
            set(whirlpool) {
                require(whirlpool.size == 64)

                System.arraycopy(whirlpool, 0, field, 0, whirlpool.size)
            }

        /**
         * Gets the version of this entry.
         * @return The version.
         */
        /**
         * Sets the version of this entry.
         * @param version The version.
         */
        /**
         * The version of this entry.
         */
        var version: Int = 0

        /**
         * The children in this entry.
         */
        val entries: SortedMap<Int, ChildEntry> = TreeMap()

        /**
         * Gets the number of actual child entries.
         * @return The number of actual child entries.
         */
        fun size(): Int {
            return entries.size
        }

        /**
         * Gets the maximum number of child entries.
         * @return The maximum number of child entries.
         */
        fun capacity(): Int {
            if (entries.isEmpty()) return 0

            return entries.lastKey() + 1
        }

        /**
         * Gets the child entry with the specified id.
         * @param id The id.
         * @return The entry, or `null` if it does not exist.
         */
        fun getEntry(id: Int): ChildEntry? {
            return entries[id]
        }

        /**
         * Replaces or inserts the child entry with the specified id.
         * @param id The id.
         * @param entry The entry.
         */
        fun putEntry(id: Int, entry: ChildEntry) {
            entries[id] = entry
        }

        /**
         * Removes the entry with the specified id.
         * @param id The id.
         * @param entry The entry.
         */
        fun removeEntry(id: Int, entry: ChildEntry?) {
            entries.remove(id)
        }
    }

    /**
     * The format of this table.
     */
    private var format = 0

    /**
     * The version of this table.
     */
    private var version = 0

    /**
     * The flags of this table.
     */
    private var flags = 0

    /**
     * The entries in this table.
     */
    private val entries: SortedMap<Int, Entry> = TreeMap()

    /**
     * The mapping for the named entries in the table.
     */
    private val namedEntries: MutableMap<Int, Int> = HashMap()

    /**
     * Gets the format of this table.
     * @return The format.
     */
    fun getFormat(): Int {
        return format
    }

    /**
     * Sets the format of this table.
     * @param format The format.
     */
    fun setFormat(format: Int) {
        this.format = format
    }

    /**
     * Gets the version of this table.
     * @return The version of this table.
     */
    fun getVersion(): Int {
        return version
    }

    /**
     * Sets the version of this table.
     * @param version The version.
     */
    fun setVersion(version: Int) {
        this.version = version
    }

    /**
     * Gets the flags of this table.
     * @return The flags.
     */
    fun getFlags(): Int {
        return flags
    }

    /**
     * Sets the flags of this table.
     * @param flags The flags.
     */
    fun setFlags(flags: Int) {
        this.flags = flags
    }

    /**
     * Gets the id for a named entry.
     * @param name The name of the entry.
     * @return The entry id or `-1` if the entry does not exist.
     */
    fun getEntryId(name: String): Int {
        val hash = StringUtils.hash(name)
        if (!namedEntries.containsKey(hash)) {
            return -1
        }
        return namedEntries[hash]!!
    }

    /**
     * Gets the entry with the specified id, or `null` if it does not
     * exist.
     * @param id The id.
     * @return The entry.
     */
    fun getEntry(id: Int): Entry? {
        return entries[id]
    }

    /**
     * Gets the child entry with the specified id, or `null` if it does
     * not exist.
     * @param id The parent id.
     * @param child The child id.
     * @return The entry.
     */
    fun getEntry(id: Int, child: Int): ChildEntry? {
        val entry = entries[id] ?: return null

        return entry.getEntry(child)
    }

    /**
     * Replaces or inserts the entry with the specified id.
     * @param id The id.
     * @param entry The entry.
     */
    fun putEntry(id: Int, entry: Entry) {
        entries[id] = entry
    }

    /**
     * Removes the entry with the specified id.
     * @param id The id.
     */
    fun removeEntry(id: Int) {
        entries.remove(id)
    }

    /**
     * Gets the number of actual entries.
     * @return The number of actual entries.
     */
    fun size(): Int {
        return entries.size
    }

    /**
     * Gets the maximum number of entries in this table.
     * @return The maximum number of entries.
     */
    fun capacity(): Int {
        if (entries.isEmpty()) return 0

        return entries.lastKey() + 1
    }

    /**
     * Encodes this [ReferenceTable] into a [ByteBuffer].
     * @return The [ByteBuffer].
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun encode(): ByteBuffer {
        /*
		 * we can't (easily) predict the size ahead of time, so we write to a
		 * stream and then to the buffer
		 */
        val bout = ByteArrayOutputStream()
        DataOutputStream(bout).use { os ->
            /* write the header */
            os.write(format)
            if (format >= 6) {
                os.writeInt(version)
            }
            os.write(flags)

            /* calculate and write the number of non-null entries */
            os.writeShort(entries.size)

            /* write the ids */
            var last = 0
            for (id in 0 until capacity()) {
                if (entries.containsKey(id)) {
                    val delta = id - last
                    os.writeShort(delta)
                    last = id
                }
            }

            /* write the identifiers if required */
            if ((flags and FLAG_IDENTIFIERS) != 0) {
                for (entry in entries.values) {
                    os.writeInt(entry.identifier)
                }
            }

            /* write the CRC checksums */
            for (entry in entries.values) {
                os.writeInt(entry.crc)
            }

            /* write the whirlpool digests if required */
            if ((flags and FLAG_WHIRLPOOL) != 0) {
                for (entry in entries.values) {
                    os.write(entry.whirlpool)
                }
            }

            /* write the versions */
            for (entry in entries.values) {
                os.writeInt(entry.version)
            }

            /* calculate and write the number of non-null child entries */
            for (entry in entries.values) {
                os.writeShort(entry.entries.size)
            }

            /* write the child ids */
            for (entry in entries.values) {
                last = 0
                for (id in 0 until entry.capacity()) {
                    if (entry.entries.containsKey(id)) {
                        val delta = id - last
                        os.writeShort(delta)
                        last = id
                    }
                }
            }

            /* write the child identifiers if required  */
            if ((flags and FLAG_IDENTIFIERS) != 0) {
                for (entry in entries.values) {
                    for (child in entry.entries.values) {
                        os.writeInt(child.identifier)
                    }
                }
            }

            /* convert the stream to a byte array and then wrap a buffer */
            val bytes = bout.toByteArray()
            return ByteBuffer.wrap(bytes)
        }
    }

    companion object {
        /**
         * A flag which indicates this [ReferenceTable] contains
         * [StringUtils] hashed identifiers.
         */
        const val FLAG_IDENTIFIERS: Int = 0x01

        /**
         * A flag which indicates this [ReferenceTable] contains
         * whirlpool digests for its entries.
         */
        const val FLAG_WHIRLPOOL: Int = 0x02

        /**
         * Decodes the slave checksum table contained in the specified
         * [ByteBuffer].
         * @param buffer The buffer.
         * @return The slave checksum table.
         */
        fun decode(buffer: ByteBuffer): ReferenceTable {
            /* create a new table */
            val table = ReferenceTable()

            /* read header */
            table.format = buffer.get().toInt() and 0xFF
            if (table.format >= 6) {
                table.version = buffer.getInt()
            }
            table.flags = buffer.get().toInt() and 0xFF

            /* read the ids */
            val ids = IntArray(buffer.getShort().toInt() and 0xFFFF)
            var accumulator = 0
            var size = -1
            for (i in ids.indices) {
                val delta = buffer.getShort().toInt() and 0xFFFF
                accumulator += delta
                ids[i] = accumulator
                if (ids[i] > size) {
                    size = ids[i]
                }
            }
            size++

            /* and allocate specific entries within that array */
            for (id in ids) {
                table.entries[id] = Entry()
            }

            /* read the identifiers if present */
            if ((table.flags and FLAG_IDENTIFIERS) != 0) {
                for (id in ids) {
                    table.entries[id]!!.identifier = buffer.getInt()
                    val identifier = table.entries[id]!!.identifier
                    table.namedEntries[identifier] = id
                }
            }

            /* read the CRC32 checksums */
            for (id in ids) {
                table.entries[id]!!.crc = buffer.getInt()
            }

            /* read the whirlpool digests if present */
            if ((table.flags and FLAG_WHIRLPOOL) != 0) {
                for (id in ids) {
                    buffer[table.entries[id]!!.whirlpool]
                }
            }

            /* read the version numbers */
            for (id in ids) {
                table.entries[id]!!.version = buffer.getInt()
            }

            /* read the child sizes */
            val members = arrayOfNulls<IntArray>(size)
            for (id in ids) {
                members[id] = IntArray(buffer.getShort().toInt() and 0xFFFF)
            }

            /* read the child ids */
            for (id in ids) {
                /* reset the accumulator and size */
                accumulator = 0
                size = -1

                /* loop through the array of ids */
                for (i in members[id]!!.indices) {
                    val delta = buffer.getShort().toInt() and 0xFFFF
                    accumulator += delta
                    members[id]!![i] = accumulator
                    if (members[id]!![i] > size) {
                        size = members[id]!![i]
                    }
                }
                size++

                /* and allocate specific entries within the array */
                for (child in members[id]!!) {
                    table.entries[id]!!.entries[child] = ChildEntry()
                }
            }

            /* read the child identifiers if present */
            if ((table.flags and FLAG_IDENTIFIERS) != 0) {
                for (id in ids) {
                    for (child in members[id]!!) {
                        table.entries[id]!!.entries[child]!!.identifier = buffer.getInt()
                    }
                }
            }

            /* return the table we constructed */
            return table
        }
    }
}