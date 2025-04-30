package net.scapeemulator.cache.def

import net.scapeemulator.cache.util.ByteBufferUtils.getJagexString
import net.scapeemulator.cache.util.ByteBufferUtils.getTriByte
import java.nio.ByteBuffer


/**
 * A class that loads item/model information from the cache.
 * @author Graham
 * @author `Discardedx2
 *
 * TODO Finish some of the opcodes.
 */
class ItemDefinition {
    var name: String? = null
        private set

    var inventoryModelId: Int = 0
        private set
    var modelZoom: Int = 0
        private set
    var modelRotation1: Int = 0
        private set
    var modelRotation2: Int = 0
        private set
    var modelOffset1: Int = 0
        private set
    var modelOffset2: Int = 0
        private set

    var isStackable: Boolean = false
        private set
    var value: Int = 0
        private set
    var isMembersOnly: Boolean = false

    var maleWearModel1: Int = -1
    var maleWearModel2: Int = 0
    var femaleWearModel1: Int = 0
        private set
    var femaleWearModel2: Int = 0
        private set

    lateinit var groundOptions: Array<String?>
        private set
    lateinit var inventoryOptions: Array<String?>
        private set

    lateinit var originalModelColors: ShortArray
        private set
    lateinit var modifiedModelColors: ShortArray
        private set
    lateinit var textureColour1: ShortArray
        private set
    lateinit var textureColour2: ShortArray
        private set
    var isUnnoted: Boolean = false
        private set

    var colourEquip1: Int = 0
        private set
    var colourEquip2: Int = 0
        private set
    var notedId: Int = 0
        private set
    var notedTemplateId: Int = 0
        private set
    var stackableIds: IntArray? = null
        private set
    lateinit var stackableAmounts: IntArray
        private set
    var teamId: Int = 0
        private set
    var lendId: Int = 0
        private set
    var lendTemplateId: Int = 0
        private set

    companion object {
        /**
         *
         * @param buffer
         * A [ByteBuffer] that contains information such as the
         * items location.
         * @return a new ItemDefinition.
         */
        @Suppress("unused")
        fun decode(buffer: ByteBuffer): ItemDefinition {
            val def = ItemDefinition()
            def.groundOptions = arrayOf(null, null, "take", null, null)
            def.inventoryOptions = arrayOf(null, null, null, null, "drop")
            while (true) {
                val opcode = buffer.get().toInt() and 0xFF
                if (opcode == 0) break
                if (opcode == 1) def.inventoryModelId = buffer.getShort().toInt() and 0xFFFF
                else if (opcode == 2) def.name = getJagexString(buffer)
                else if (opcode == 4) def.modelZoom = buffer.getShort().toInt() and 0xFFFF
                else if (opcode == 5) def.modelRotation1 = buffer.getShort().toInt() and 0xFFFF
                else if (opcode == 6) def.modelRotation2 = buffer.getShort().toInt() and 0xFFFF
                else if (opcode == 7) {
                    def.modelOffset1 = buffer.getShort().toInt() and 0xFFFF
                    if (def.modelOffset1 > 32767) def.modelOffset1 -= 65536
                    def.modelOffset1 = def.modelOffset1 shl 0
                } else if (opcode == 8) {
                    def.modelOffset2 = buffer.getShort().toInt() and 0xFFFF
                    if (def.modelOffset2 > 32767) def.modelOffset2 -= 65536
                    def.modelOffset2 = def.modelOffset2 shl 0
                } else if (opcode == 11) def.isStackable = true
                else if (opcode == 12) def.value = buffer.getInt()
                else if (opcode == 16) def.isMembersOnly = true
                else if (opcode == 18) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 23) def.maleWearModel1 = buffer.getShort().toInt() and 0xFFFF
                else if (opcode == 24) def.femaleWearModel1 = buffer.getShort().toInt() and 0xFFFF
                else if (opcode == 25) def.maleWearModel2 = buffer.getShort().toInt() and 0xFFFF
                else if (opcode == 26) def.femaleWearModel2 = buffer.getShort().toInt() and 0xFFFF
                else if (opcode >= 30 && opcode < 35) def.groundOptions[opcode - 30] = getJagexString(buffer)
                else if (opcode >= 35 && opcode < 40) def.inventoryOptions[opcode - 35] = getJagexString(buffer)
                else if (opcode == 40) {
                    val length = buffer.get().toInt() and 0xFF
                    def.originalModelColors = ShortArray(length)
                    def.modifiedModelColors = ShortArray(length)
                    for (index in 0..<length) {
                        def.originalModelColors[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                        def.modifiedModelColors[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    }
                } else if (opcode == 41) {
                    val length = buffer.get().toInt() and 0xFF
                    def.textureColour1 = ShortArray(length)
                    def.textureColour2 = ShortArray(length)
                    for (index in 0..<length) {
                        def.textureColour1[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                        def.textureColour2[index] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    }
                } else if (opcode == 42) {
                    val length = buffer.get().toInt() and 0xFF
                    for (index in 0..<length) {
                        val i = buffer.get().toInt()
                    }
                } else if (opcode == 65) {
                    def.isUnnoted = true
                } else if (opcode == 78) {
                    def.colourEquip1 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 79) {
                    def.colourEquip2 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 90) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 91) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 92) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 93) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 95) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 96) {
                    val i = buffer.get().toInt() and 0xFF
                } else if (opcode == 97) {
                    def.notedId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 98) {
                    def.notedTemplateId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode >= 100 && opcode < 110) {
                    if (def.stackableIds == null) {
                        def.stackableIds = IntArray(10)
                        def.stackableAmounts = IntArray(10)
                    }
                    def.stackableIds!![opcode - 100] = buffer.getShort().toInt() and 0xFFFF
                    def.stackableAmounts[opcode - 100] = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 110) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 111) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 112) {
                    val i = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 113) {
                    val i = buffer.get().toInt()
                } else if (opcode == 114) {
                    val i = buffer.get() * 5
                } else if (opcode == 115) {
                    def.teamId = buffer.get().toInt() and 0xFF
                } else if (opcode == 121) {
                    def.lendId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 122) {
                    def.lendTemplateId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 125) {
                    val i = buffer.get().toInt() shl 0
                    val i2 = buffer.get().toInt() shl 0
                    val i3 = buffer.get().toInt() shl 0
                } else if (opcode == 126) {
                    val i = buffer.get().toInt() shl 0
                    val i2 = buffer.get().toInt() shl 0
                    val i3 = buffer.get().toInt() shl 0
                } else if (opcode == 127) {
                    val i = buffer.get().toInt() and 0xFF
                    val i2 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 128) {
                    val i = buffer.get().toInt() and 0xFF
                    val i2 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 129) {
                    val i = buffer.get().toInt() and 0xFF
                    val i2 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 130) {
                    val i = buffer.get().toInt() and 0xFF
                    val i2 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 132) {
                    val len = buffer.get().toInt() and 0xFF
                    for (index in 0..<len) {
                        val anInt = buffer.getShort().toInt() and 0xFFFF
                    }
                } else if (opcode == 249) {
                    val length = buffer.get().toInt() and 0xFF
                    for (index in 0..<length) {
                        val stringInstance = buffer.get().toInt() == 1
                        val key = getTriByte(buffer)
                        val value: Any? = if (stringInstance) getJagexString(buffer) else buffer.getInt()
                    }
                }
            }
            return def
        }
    }
}
