package net.scapeemulator.util

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.math.BigInteger
import java.nio.charset.StandardCharsets

object ByteBufUtils {
    fun rsa(buf: ByteBuf, modulus: String, exponent: String): ByteBuf {
//        val bytes = ByteArray(buf.readByte().toInt())
        val bytes = ByteArray(buf.readableBytes())
        buf.readBytes(bytes)

        val cipherText = BigInteger(bytes)
        val mod = BigInteger(modulus)
        val exp = BigInteger(exponent)
        val plainText = cipherText.modPow(exp, mod)

        return Unpooled.wrappedBuffer(plainText.toByteArray())
    }

    fun readString(buffer: ByteBuf): String {
        buffer.markReaderIndex()

        var len = 0
        while (buffer.readUnsignedByte().toInt() != 0) len++

        buffer.resetReaderIndex()

        val bytes = ByteArray(len)
        buffer.readBytes(bytes)
        buffer.readerIndex(buffer.readerIndex() + 1)
        return String(bytes, StandardCharsets.ISO_8859_1)
    }

    fun writeString(buffer: ByteBuf, str: String) {
        val bytes = str.toByteArray(StandardCharsets.ISO_8859_1)
        buffer.writeBytes(bytes)
        buffer.writeByte(0)
    }

    //todo
    //fun GameFrameBuilder.writeSmart
    fun writeSmart(buffer: ByteBuf, value: Int) {
        if (value < 128) buffer.writeByte(value)
        else buffer.writeShort(32768 + value)
    }

    fun writeWorldListString(buffer: ByteBuf, str: String) {
        buffer.writeByte(0)
        buffer.writeBytes(str.toByteArray(StandardCharsets.ISO_8859_1))
        buffer.writeByte(0)
    }
}
