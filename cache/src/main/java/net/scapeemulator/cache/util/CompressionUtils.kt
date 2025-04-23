package net.scapeemulator.cache.util

import org.apache.tools.bzip2.CBZip2InputStream
import org.apache.tools.bzip2.CBZip2OutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * A class that contains methods to compress and uncompress BZIP2 and GZIP
 * byte arrays.
 * @author Graham
 * @author `Discardedx2
 */
object CompressionUtils {
    /**
     * Compresses a GZIP file.
     * @param bytes The uncompressed bytes.
     * @return The compressed bytes.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun gzip(bytes: ByteArray): ByteArray {
        /* create the streams */
        ByteArrayInputStream(bytes).use { `is` ->
            val bout = ByteArrayOutputStream()
            GZIPOutputStream(bout).use { os ->
                /* copy data between the streams */
                val buf = ByteArray(4096)
                var len: Int
                while ((`is`.read(buf, 0, buf.size).also { len = it }) != -1) {
                    os.write(buf, 0, len)
                }
            }
            /* return the compressed bytes */
            return bout.toByteArray()
        }
    }

    /**
     * Uncompresses a GZIP file.
     * @param bytes The compressed bytes.
     * @return The uncompressed bytes.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun gunzip(bytes: ByteArray): ByteArray {
        /* create the streams */
        GZIPInputStream(ByteArrayInputStream(bytes)).use { `is` ->
            ByteArrayOutputStream().use { os ->
                /* copy data between the streams */
                val buf = ByteArray(4096)
                var len: Int
                while ((`is`.read(buf, 0, buf.size).also { len = it }) != -1) {
                    os.write(buf, 0, len)
                }

                /* return the uncompressed bytes */
                return os.toByteArray()
            }
        }
    }

    /**
     * Compresses a BZIP2 file.
     * @param bytes The uncompressed bytes.
     * @return The compressed bytes without the header.
     * @throws IOException if an I/O erorr occurs.
     */
    @Throws(IOException::class)
    fun bzip2(bytes: ByteArray): ByteArray {
        var bytes = bytes
        ByteArrayInputStream(bytes).use { `is` ->
            val bout = ByteArrayOutputStream()
            CBZip2OutputStream(bout, 1).use { os ->
                val buf = ByteArray(4096)
                var len: Int
                while ((`is`.read(buf, 0, buf.size).also { len = it }) != -1) {
                    os.write(buf, 0, len)
                }
            }
            /* strip the header from the byte array and return it */
            bytes = bout.toByteArray()
            val bzip2 = ByteArray(bytes.size - 2)
            System.arraycopy(bytes, 2, bzip2, 0, bzip2.size)
            return bzip2
        }
    }

    /**
     * Uncompresses a BZIP2 file.
     * @param bytes The compressed bytes without the header.
     * @return The uncompressed bytes.
     * @throws IOException if an I/O error occurs.
     */
    @Throws(IOException::class)
    fun bunzip2(bytes: ByteArray): ByteArray {
        /* prepare a new byte array with the bzip2 header at the start */
        val bzip2 = ByteArray(bytes.size + 2)
        bzip2[0] = 'h'.code.toByte()
        bzip2[1] = '1'.code.toByte()
        System.arraycopy(bytes, 0, bzip2, 2, bytes.size)

        CBZip2InputStream(ByteArrayInputStream(bzip2)).use { `is` ->
            ByteArrayOutputStream().use { os ->
                val buf = ByteArray(4096)
                var len: Int
                while ((`is`.read(buf, 0, buf.size).also { len = it }) != -1) {
                    os.write(buf, 0, len)
                }
                return os.toByteArray()
            }
        }
    }
}
