package net.scapeemulator.cache.util

import java.io.EOFException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * Contains [FileChannel]-related utility methods.
 * @author Graham
 * @author `Discardedx2
 */
object FileChannelUtils {
    /**
     * Reads as much as possible from the channel into the buffer.
     * @param channel The channel.
     * @param buffer The buffer.
     * @param ptr The initial position in the channel.
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if the end of the file was reached and the buffer
     * could not be completely populated.
     */
    @Throws(IOException::class)
    fun readFully(channel: FileChannel, buffer: ByteBuffer, ptr: Long) {
        var ptr = ptr
        while (buffer.remaining() > 0) {
            val read = channel.read(buffer, ptr).toLong()
            if (read == -1L) {
                throw EOFException()
            } else {
                ptr += read
            }
        }
    }
}
