package net.scapeemulator.cache

import net.scapeemulator.cache.util.crypto.Rsa.crypt
import net.scapeemulator.cache.util.crypto.Whirlpool
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * A [ChecksumTable] stores checksums and versions of
 * [ReferenceTable]s. When encoded in a [Container] and prepended
 * with the file type and id it is more commonly known as the client's
 * "update keys".
 * @author Graham
 * @author `Discardedx2
 */
class ChecksumTable(size: Int) {
    /**
     * Represents a single entry in a [ChecksumTable]. Each entry
     * contains a CRC32 checksum and version of the corresponding
     * [ReferenceTable].
     * @author Graham Edgecombe
     */
    class Entry(crc: Int, version: Int, whirlpool: ByteArray) {
        /**
         * Gets the CRC32 checksum of the reference table.
         * @return The CRC32 checksum.
         */
        /**
         * The CRC32 checksum of the reference table.
         */
        val crc: Int

        /**
         * Gets the version of the reference table.
         * @return The version.
         */
        /**
         * The version of the reference table.
         */
        val version: Int

        /**
         * Gets the whirlpool digest of the reference table.
         * @return The whirlpool digest.
         */
        /**
         * The whirlpool digest of the reference table.
         */
        val whirlpool: ByteArray

        /**
         * Creates a new entry.
         * @param crc The CRC32 checksum of the slave table.
         * @param version The version of the slave table.
         * @param whirlpool The whirlpool digest of the reference table.
         */
        init {
            require(whirlpool.size == 64)

            this.crc = crc
            this.version = version
            this.whirlpool = whirlpool
        }
    }

    /**
     * The entries in this table.
     */

    private val entries: Array<Entry>

    /**
     * Creates a new [ChecksumTable] with the specified size.
     * @param size The number of entries in this table.
     */
    init {
        @Suppress("UNCHECKED_CAST")
        entries = arrayOfNulls<Entry>(size) as Array<Entry>
    }

    /**
     * Encodes this [ChecksumTable] and encrypts the final whirlpool hash.
     * @param whirlpool If whirlpool digests should be encoded.
     * @param modulus The modulus.
     * @param privateKey The private key.
     * @return The encoded [ByteBuffer].
     * @throws IOException if an I/O error occurs.
     */
    /**
     * Encodes this [ChecksumTable].
     * @param whirlpool If whirlpool digests should be encoded.
     * @return The encoded [ByteBuffer].
     * @throws IOException if an I/O error occurs.
     */
    /**
     * Encodes this [ChecksumTable]. Whirlpool digests are not encoded.
     * @return The encoded [ByteBuffer].
     * @throws IOException if an I/O error occurs.
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun encode(whirlpool: Boolean = false, modulus: BigInteger? = null, privateKey: BigInteger? = null): ByteBuffer {
        val bout = ByteArrayOutputStream()
        DataOutputStream(bout).use { os ->
            /* as the new whirlpool format is more complicated we must write the number of entries */
            if (whirlpool) os.write(entries.size)

            /* encode the individual entries */
            for (entry in entries) {
                os.writeInt(entry.crc)
                os.writeInt(entry.version)
                if (whirlpool) os.write(entry.whirlpool)
            }

            /* compute (and encrypt) the digest of the whole table */
            if (whirlpool) {
                var bytes = bout.toByteArray()
                var temp = ByteBuffer.allocate(65)
                temp.put(0.toByte())
                temp.put(Whirlpool.whirlpool(bytes, 0, bytes.size))
                temp.flip()

                if (modulus != null && privateKey != null) {
                    temp = crypt(temp, modulus, privateKey)
                }

                bytes = ByteArray(temp.limit())
                temp.get(bytes)
                os.write(bytes)
            }

            val bytes = bout.toByteArray()
            return ByteBuffer.wrap(bytes)
        }
    }

    val size: Int
        /**
         * Gets the size of this table.
         * @return The size of this table.
         */
        get() = entries.size

    /**
     * Sets an entry in this table.
     * @param id The id.
     * @param entry The entry.
     * @throws IndexOutOfBoundsException if the id is less than zero or greater
     * than or equal to the size of the table.
     */
    fun setEntry(id: Int, entry: Entry?) {
        if (id < 0 || id >= entries.size) throw IndexOutOfBoundsException()
        entries[id] = entry!!
    }

    /**
     * Gets an entry from this table.
     * @param id The id.
     * @return The entry.
     * @throws IndexOutOfBoundsException if the id is less than zero or greater
     * than or equal to the size of the table.
     */
    fun getEntry(id: Int): Entry {
        if (id < 0 || id >= entries.size) throw IndexOutOfBoundsException()
        return entries[id]
    }

    companion object {
        /**
         * Decodes the [ChecksumTable] in the specified
         * [ByteBuffer] and decrypts the final whirlpool hash.
         * @param buffer The [ByteBuffer] containing the table.
         * @param whirlpool If whirlpool digests should be read.
         * @param modulus The modulus.
         * @param publicKey The public key.
         * @return The decoded [ChecksumTable].
         * @throws IOException if an I/O error occurs.
         */
        /**
         * Decodes the [ChecksumTable] in the specified
         * [ByteBuffer].
         * @param buffer The [ByteBuffer] containing the table.
         * @param whirlpool If whirlpool digests should be read.
         * @return The decoded [ChecksumTable].
         * @throws IOException if an I/O error occurs.
         */
        /**
         * Decodes the [ChecksumTable] in the specified
         * [ByteBuffer]. Whirlpool digests are not read.
         * @param buffer The [ByteBuffer] containing the table.
         * @return The decoded [ChecksumTable].
         * @throws IOException if an I/O error occurs.
         */
        @JvmOverloads
        @Throws(IOException::class)
        fun decode(
            buffer: ByteBuffer,
            whirlpool: Boolean = false,
            modulus: BigInteger? = null,
            publicKey: BigInteger? = null
        ): ChecksumTable {
            /* find out how many entries there are and allocate a new table */
            val size = if (whirlpool) (buffer.get().toInt() and 0xFF) else (buffer.limit() / 8)
            val table = ChecksumTable(size)

            /* calculate the whirlpool digest we expect to have at the end */
            var masterDigest: ByteArray? = null
            if (whirlpool) {
                val temp = ByteArray(size * 72 + 1)
                buffer.position(0)
                buffer.get(temp)
                masterDigest = Whirlpool.whirlpool(temp, 0, temp.size)
            }

            /* read the entries */
            buffer.position(if (whirlpool) 1 else 0)
            for (i in 0..<size) {
                val crc = buffer.getInt()
                val version = buffer.getInt()
                val digest = ByteArray(64)
                if (whirlpool) {
                    buffer.get(digest)
                }
                table.entries[i] = Entry(crc, version, digest)
            }

            /* read the trailing digest and check if it matches up */
            if (whirlpool) {
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                var temp = ByteBuffer.wrap(bytes)

                if (modulus != null && publicKey != null) {
                    temp = crypt(buffer, modulus, publicKey)
                }

                if (temp.limit() != 65) throw IOException("Decrypted data is not 65 bytes long")

                for (i in 0..63) {
                    if (temp.get(i + 1) != masterDigest!![i]) throw IOException("Whirlpool digest mismatch")
                }
            }

            /* if it looks good return the table */
            return table
        }
    }
}
