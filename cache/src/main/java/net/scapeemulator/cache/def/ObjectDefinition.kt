package net.scapeemulator.cache.def

import net.scapeemulator.cache.util.ByteBufferUtils
import java.nio.ByteBuffer

class ObjectDefinition {
    var name: String? = null
        private set
    var options: Array<String?> = emptyArray()
        private set
    var width: Int = 0
        private set
    var length: Int = 0
        private set
    var animationId: Int = 0
        private set
    var isImpenetrable: Boolean = false
        private set
    var isSolid: Boolean = false
        private set
    var clipType: Int = 0
    var children: Array<Int?> = emptyArray()
        private set
    var membersOnly: Boolean = false

    fun hasOptions(): Boolean {
        for (i in options.indices)
            if (options[i] != null)
                return true
        return false
    }

    companion object {
        fun decode(buffer: ByteBuffer): ObjectDefinition {
            val def = ObjectDefinition()
            def.name = "null"
            def.width = 1
            def.length = 1
            def.options = arrayOfNulls(5)
            def.isImpenetrable = true
            def.isSolid = true
            while (true) {
                val opcode = buffer.get().toInt() and 0xFF
                if (opcode == 0) break
                if (opcode == 1) {
                    val i = buffer.get().toInt() and 0xff
                    if (i > 0)
                        for (var5 in 0 until i) {
                            buffer.getShort()
                            buffer.get()
                        }
                } else if (opcode == 2) {
                    def.name = ByteBufferUtils.getJagexString(buffer)
                } else if (opcode == 5) {
                    val i = buffer.get().toInt() and 0xff
                    if (i > 0) for (var5 in 0 until i)
                        buffer.getShort()
                } else if (opcode == 14) {
                    def.width = buffer.get().toInt() and 0xff
                } else if (opcode == 15) {
                    def.length = buffer.get().toInt() and 0xff
                } else if (opcode == 17) {
                    def.isSolid = false
                    //TODO
                    def.isImpenetrable = false
                    def.clipType = 0
                } else if (opcode == 18) {
                    def.isImpenetrable = false
                } else if (opcode == 19) {
                    val i = buffer.get().toInt()
                } else if (opcode == 24) {
                    def.animationId = buffer.getShort().toInt()
                    if (def.animationId == 65535) def.animationId = -1
                } else if (opcode == 27) {
                    def.clipType = 1
                } else if (opcode == 28) {
                    val i = buffer.get().toInt()
                } else if (opcode == 29) {
                    val i = buffer.get().toInt()
                } else if (opcode >= 30 && opcode < 35)
                    def.options[opcode - 30] = ByteBufferUtils.getJagexString(buffer)
                else if (opcode == 39) {
                    val i = buffer.get().toInt()
                } else if (opcode == 40) {
                    val length = buffer.get().toInt() and 0xFF
                    for (index in 0 until length) {
                        buffer.getShort()
                        buffer.getShort()
                    }
                } else if (opcode == 41) {
                    val length = buffer.get().toInt() and 0xFF
                    for (index in 0 until length) {
                        val i = buffer.getShort().toInt() and 0xFFFFF
                        val i2 = buffer.getShort().toInt() and 0xFFFFF
                    }
                } else if (opcode == 42) {
                    val length = buffer.get().toInt() and 0xFF
                    for (index in 0 until length) {
                        val i = buffer.get().toInt()
                    }
                } else if (opcode == 60) {
                    val i = buffer.getShort().toInt() and 0xffff
                    //TODO mapicon
                    //def.mapIcon = buffer.getShort();
                } else if (opcode == 65) {
                    val i = buffer.getShort().toInt() and 0xffff
                } else if (opcode == 66) {
                    val i = buffer.getShort().toInt() and 0xffff
                } else if (opcode == 67) {
                    val i = buffer.getShort().toInt() and 0xffff
                } else if (opcode == 69) {
                    val walkingFlag = buffer.get().toInt() and 0xff
                } else if (opcode == 70) {
                    val i = buffer.getShort().toInt()
                } else if (opcode == 71) {
                    val i = buffer.getShort().toInt()
                } else if (opcode == 72) {
                    val i = buffer.getShort().toInt()
                } else if (opcode == 74) {
                    def.clipType = 0
                    def.isImpenetrable = false
                } else if (opcode == 75) {
                    val i = buffer.get().toInt()
                } else if (opcode == 77 || opcode == 92) {
                    val configFileId = buffer.getShort().toInt() and 0xffff
                    val configId = buffer.getShort().toInt() and 0xffff
                    var defaultId = -1
                    if (opcode == 92) {//defaultId
                        defaultId = buffer.getShort().toInt()
                        if (defaultId == 65535) defaultId = -1
                    }
                    val childrenAmount = buffer.get().toInt() and 0xff
                    def.children = arrayOfNulls(childrenAmount + 2)
                    for (index in 0..childrenAmount) {
                        val i5 = buffer.getShort().toInt()
                        def.children[index] = if (i5 == 65535) -1 else i5
                    }
                    def.children[childrenAmount + 1] = defaultId
                } else if (opcode == 78) {
                    buffer.getShort()
                    buffer.get()
                } else if (opcode == 79) {
                    val i = buffer.getShort().toInt() and 0xffff
                    val i2 = buffer.getShort().toInt() and 0xffff
                    val i3 = buffer.get().toInt() and 0xff
                    val i4 = buffer.get().toInt() and 0xff
                    for (counter in 0 until i4) {
                        val i5 = buffer.getShort().toInt() and 0xffff
                    }
                } else if (opcode == 81) {
                    val i = buffer.get().toInt() and 0xff
                } else if (opcode == 91) {
                    //TODO
                    def.membersOnly = true
                } else if (opcode == 93) {
                    val i = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 99) {
                    val i = buffer.get().toInt() and 0xff
                    val i2 = buffer.getShort().toInt() and 0xffff
                } else if (opcode == 100) {
                    val i = buffer.get().toInt() and 0xff
                    val i2 = buffer.getShort().toInt() and 0xffff
                } else if (opcode == 101) {
                    val i = buffer.get().toInt() and 0xff
                } else if (opcode == 102) {
                    val i = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 249) {
                    val length = buffer.get().toInt() and 0xFF
                    for (index in 0 until length) {
                        val stringInstance = buffer.get().toInt() == 1
                        val key = ByteBufferUtils.getTriByte(buffer)
                        val value: Any = if (stringInstance) ByteBufferUtils.getJagexString(buffer) else buffer.getInt()
                    }
                }
            }
            return def
        }
    }
}