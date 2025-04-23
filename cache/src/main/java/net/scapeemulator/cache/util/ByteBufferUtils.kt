package net.scapeemulator.cache.util

import net.scapeemulator.cache.util.crypto.Whirlpool
import java.nio.ByteBuffer
import java.util.Locale
import java.util.zip.CRC32
import java.util.zip.Checksum
import kotlin.ByteArray
import kotlin.CharArray
import kotlin.Int
import kotlin.String
import kotlin.also
import kotlin.charArrayOf
import kotlin.code

/**
 * Contains [ByteBuffer]-related utility methods.
 * @author Graham
 * @author `Discardedx2
 */
object ByteBufferUtils {
    /**
     * The modified set of 'extended ASCII' characters used by the client.
     */
    private val CHARACTERS: CharArray? = charArrayOf(
        '\u20AC', '\u0000', '\u201A', '\u0192',
        '\u201E', '\u2026', '\u2020', '\u2021', '\u02C6', '\u2030',
        '\u0160', '\u2039', '\u0152', '\u0000', '\u017D', '\u0000', '\u0000', '\u2018',
        '\u2019', '\u201C', '\u201D', '\u2022', '\u2013', '\u2014',
        '\u02DC', '\u2122', '\u0161', '\u203A', '\u0153', '\u0000', '\u017E',
        '\u0178'
    )

    @JvmStatic
	fun getString(buf: ByteBuffer): String {
        val bldr = StringBuilder()
        var b: Int
        while (((buf.get().toInt() and 0xFF).also { b = it }) != 0) {
            bldr.append(b.toChar())
        }
        return bldr.toString()
    }

    /**
     * Gets a null-terminated string from the specified buffer, using a
     * modified ISO-8859-1 character set.
     * @param buf The buffer.
     * @return The decoded string.
     */
    fun getJagexString(buf: ByteBuffer): String {
        val bldr = StringBuilder()
        var b: Int
        while (((buf.get().toInt() and 0xFF).also { b = it }) != 0) {
            if (b >= 127 && b < 160) {
                val curChar = CHARACTERS!![b - 128]
                if (curChar.code != 0) {
                    bldr.append(curChar)
                }
            } else {
                bldr.append(b.toChar())
            }
        }
        return bldr.toString()
    }

    /**
     * Reads a 'tri-byte' from the specified buffer.
     * @param buf The buffer.
     * @return The value.
     */
	@JvmStatic
	fun getTriByte(buf: ByteBuffer): Int {
        return ((buf.get().toInt() and 0xFF) shl 16) or ((buf.get().toInt() and 0xFF) shl 8) or (buf.get()
            .toInt() and 0xFF)
    }

    /**
     * Writes a 'tri-byte' to the specified buffer.
     * @param buf The buffer.
     * @param value The value.
     */
    fun putTriByte(buf: ByteBuffer, value: Int) {
        buf.put((value shr 16).toByte())
        buf.put((value shr 8).toByte())
        buf.put(value.toByte())
    }

    /**
     * Calculates the CRC32 checksum of the specified buffer.
     * @param buffer The buffer.
     * @return The CRC32 checksum.
     */
    @JvmStatic
    fun getCrcChecksum(buffer: ByteBuffer): Int {
        val crc: Checksum = CRC32()
        for (i in 0..<buffer.limit()) {
            crc.update(buffer.get(i).toInt())
        }
        return crc.getValue().toInt()
    }

    /**
     * Calculates the whirlpool digest of the specified buffer.
     * @param buf The buffer.
     * @return The 64-byte whirlpool digest.
     */
    @JvmStatic
    fun getWhirlpoolDigest(buf: ByteBuffer): ByteArray {
        val bytes = ByteArray(buf.limit())
        buf.get(bytes)
        return Whirlpool.whirlpool(bytes, 0, bytes.size)
    }

    fun getSmart(buf: ByteBuffer): Int {
        val peek = buf.get(buf.position()).toInt() and 0xFF
        if (peek < 128) {
            return buf.get().toInt() and 0xFF
        } else {
            return (buf.getShort().toInt() and 0xFFFF) - 32768
        }
    }

    /**
     * Converts the contents of the specified byte buffer to a string, which is
     * formatted similarly to the output of the [Arrays.toString]
     * method.
     * @param buffer The buffer.
     * @return The string.
     */
    fun toString(buffer: ByteBuffer): String {
        val builder = StringBuilder("[")
        for (i in 0..<buffer.limit()) {
            var hex = Integer.toHexString(buffer.get(i).toInt() and 0xFF).uppercase(Locale.getDefault())
            if (hex.length == 1) hex = "0" + hex

            builder.append("0x").append(hex)
            if (i != buffer.limit() - 1) {
                builder.append(", ")
            }
        }
        builder.append("]")
        return builder.toString()
    }
}
