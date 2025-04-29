package net.scapeemulator.game.net.game

import io.netty.buffer.ByteBuf
import net.scapeemulator.util.ByteBufUtils
import kotlin.math.pow

class GameFrameReader(frame: GameFrame) {
    private val buffer: ByteBuf = frame.payload
    private var mode = AccessMode.BYTE_ACCESS
    private var bitIndex = 0

    val length: Int
        get() {
            checkByteAccess()
            return buffer.readableBytes()
        }

    fun switchToByteAccess() {
        check(mode != AccessMode.BYTE_ACCESS) { "Already in byte access mode" }
        mode = AccessMode.BYTE_ACCESS
        buffer.readerIndex((bitIndex + 7) / 8)
    }

    fun switchToBitAccess() {
        check(mode != AccessMode.BIT_ACCESS) { "Already in bit access mode" }
        mode = AccessMode.BIT_ACCESS
        bitIndex = buffer.readerIndex() * 8
    }

    val string: String
        get() {
            checkByteAccess()
            return ByteBufUtils.readString(buffer)
        }

    val signedSmart: Int
        get() {
            checkByteAccess()
            val peek = buffer.getByte(buffer.readerIndex()).toInt()
            return if (peek < 128) {
                buffer.readByte() - 64
            } else {
                buffer.readShort() - 49152
            }
        }

    val unsignedSmart: Int
        get() {
            checkByteAccess()
            val peek = buffer.getByte(buffer.readerIndex()).toInt()
            return if (peek < 128) {
                buffer.readByte().toInt()
            } else {
                buffer.readShort() - 32768
            }
        }

    fun getSigned(type: DataType): Long {
        return getSigned(type, DataOrder.BIG, DataTransformation.NONE)
    }

    fun getSigned(type: DataType, order: DataOrder): Long {
        return getSigned(type, order, DataTransformation.NONE)
    }

    fun getSigned(type: DataType, transformation: DataTransformation): Long {
        return getSigned(type, DataOrder.BIG, transformation)
    }

    fun getSigned(type: DataType, order: DataOrder, transformation: DataTransformation): Long {
        var longValue = get(type, order, transformation)
        if (type != DataType.LONG) {
            val max = (2.0.pow((type.bytes * 8 - 1).toDouble()) - 1).toInt()
            if (longValue > max) {
                longValue -= ((max + 1) * 2).toLong()
            }
        }
        return longValue
    }

    fun getUnsigned(type: DataType): Long {
        return getUnsigned(type, DataOrder.BIG, DataTransformation.NONE)
    }

    fun getUnsigned(type: DataType, order: DataOrder): Long {
        return getUnsigned(type, order, DataTransformation.NONE)
    }

    fun getUnsigned(type: DataType, transformation: DataTransformation): Long {
        return getUnsigned(type, DataOrder.BIG, transformation)
    }

    fun getUnsigned(type: DataType, order: DataOrder, transformation: DataTransformation): Long {
        val longValue = get(type, order, transformation)
        require(type != DataType.LONG) { "due to java restrictions, longs must be read as signed types" }
        return longValue and -0x1L
    }

    private fun get(type: DataType, order: DataOrder, transformation: DataTransformation): Long {
        checkByteAccess()
        var longValue: Long = 0
        val length = type.bytes
        if (order == DataOrder.BIG) {
            for (i in length - 1 downTo 0) {
                longValue = if (i == 0 && transformation != DataTransformation.NONE) {
                    if (transformation == DataTransformation.ADD) {
                        longValue or ((buffer.readByte() - 128) and 0xFF).toLong()
                    } else if (transformation == DataTransformation.NEGATE) {
                        longValue or ((-buffer.readByte()) and 0xFF).toLong()
                    } else if (transformation == DataTransformation.SUBTRACT) {
                        longValue or ((128 - buffer.readByte()) and 0xFF).toLong()
                    } else {
                        throw IllegalArgumentException("unknown transformation")
                    }
                } else {
                    longValue or ((buffer.readByte().toInt() and 0xFF) shl (i * 8)).toLong()
                }
            }
        } else if (order == DataOrder.LITTLE) {
            for (i in 0..<length) {
                longValue = if (i == 0 && transformation != DataTransformation.NONE) {
                    if (transformation == DataTransformation.ADD) {
                        longValue or ((buffer.readByte() - 128) and 0xFF).toLong()
                    } else if (transformation == DataTransformation.NEGATE) {
                        longValue or ((-buffer.readByte()) and 0xFF).toLong()
                    } else if (transformation == DataTransformation.SUBTRACT) {
                        longValue or ((128 - buffer.readByte()) and 0xFF).toLong()
                    } else {
                        throw IllegalArgumentException("unknown transformation")
                    }
                } else {
                    longValue or ((buffer.readByte().toInt() and 0xFF) shl (i * 8)).toLong()
                }
            }
        } else if (order == DataOrder.MIDDLE) {
            require(transformation == DataTransformation.NONE) { "middle endian cannot be transformed" }
            require(type == DataType.INT) { "middle endian can only be used with an integer" }
            longValue = longValue or ((buffer.readByte().toInt() and 0xFF) shl 8).toLong()
            longValue = longValue or (buffer.readByte().toInt() and 0xFF).toLong()
            longValue = longValue or ((buffer.readByte().toInt() and 0xFF) shl 24).toLong()
            longValue = longValue or ((buffer.readByte().toInt() and 0xFF) shl 16).toLong()
        } else if (order == DataOrder.INVERSED_MIDDLE) {
            require(transformation == DataTransformation.NONE) { "inversed middle endian cannot be transformed" }
            require(type == DataType.INT) { "inversed middle endian can only be used with an integer" }
            longValue = longValue or ((buffer.readByte().toInt() and 0xFF) shl 16).toLong()
            longValue = longValue or ((buffer.readByte().toInt() and 0xFF) shl 24).toLong()
            longValue = longValue or (buffer.readByte().toInt() and 0xFF).toLong()
            longValue = longValue or ((buffer.readByte().toInt() and 0xFF) shl 8).toLong()
        } else {
            throw IllegalArgumentException("unknown order")
        }
        return longValue
    }

    fun getBytes(bytes: ByteArray) {
        checkByteAccess()
        for (i in bytes.indices) {
            bytes[i] = buffer.readByte()
        }
    }

    fun getBytes(transformation: DataTransformation, bytes: ByteArray) {
        if (transformation == DataTransformation.NONE) {
            getBytesReverse(bytes)
        } else {
            for (i in bytes.indices) {
                bytes[i] = getSigned(DataType.BYTE, transformation).toByte()
            }
        }
    }

    fun getBytesReverse(bytes: ByteArray) {
        checkByteAccess()
        for (i in bytes.indices.reversed()) {
            bytes[i] = buffer.readByte()
        }
    }

    fun getBytesReverse(transformation: DataTransformation, bytes: ByteArray) {
        if (transformation == DataTransformation.NONE) {
            getBytesReverse(bytes)
        } else {
            for (i in bytes.indices.reversed()) {
                bytes[i] = getSigned(DataType.BYTE, transformation).toByte()
            }
        }
    }

    private fun checkByteAccess() {
        check(mode == AccessMode.BYTE_ACCESS) { "For byte-based calls to work, the mode must be byte access" }
    }

    private fun checkBitAccess() {
        check(mode == AccessMode.BIT_ACCESS) { "For bit-based calls to work, the mode must be bit access" }
    }

    val bit: Int
        get() = getBits(1)

    fun getBits(numBits: Int): Int {
        var numBits = numBits
        require(!(numBits < 0 || numBits > 32)) { "Number of bits must be between 1 and 32 inclusive" }

        checkBitAccess()

        var bytePos = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        var value = 0
        bitIndex += numBits

        while (numBits > bitOffset) {
            value += (buffer.getByte(bytePos++).toInt() and BITMASKS[bitOffset]) shl numBits - bitOffset
            numBits -= bitOffset
            bitOffset = 8
        }
        value += if (numBits == bitOffset) {
            buffer.getByte(bytePos).toInt() and BITMASKS[bitOffset]
        } else {
            buffer.getByte(bytePos).toInt() shr bitOffset - numBits and BITMASKS[numBits]
        }
        return value
    }

    companion object {
        private val BITMASKS = intArrayOf(
            0x0, 0x1, 0x3, 0x7,
            0xf, 0x1f, 0x3f, 0x7f,
            0xff, 0x1ff, 0x3ff, 0x7ff,
            0xfff, 0x1fff, 0x3fff, 0x7fff,
            0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
            0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
            0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
            0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
            -1
        )
    }
}
