package net.scapeemulator.cache.def

import net.scapeemulator.cache.util.ByteBufferUtils
import java.nio.ByteBuffer

/**
 * A class that can decode and decode item definitions from the cache.
 *
 * @author Graham
 * @author `Discardedx2
 * @author Davidi2
 */
class ItemDefinition {
    var id: Int = 0
        private set
    private var name = "null"
    private var examine: String? = null
    private var inventoryModelId = 0
    private var modelZoom = 2000
    private var modelRotation1 = 0
    private var modelRotation2 = 0
    private var modelOffset1 = 0
    private var modelOffset2 = 0

    private var stackable = false
    private var value = 1
    private var membersOnly = false

    private var scriptData: MutableMap<Int, Any>

    private var maleWearModel1: Int
    private var maleWearModel2: Int
    private var femaleWearModel1: Int
    private var femaleWearModel2: Int
    private var maleDialogueHat: Int
    private var femaleDialogueHat: Int
    private var maleDialogueModel: Int
    private var femaleDialogueModel: Int
    var colourEquip1: Int
        private set
    var colourEquip2: Int
        private set

    var groundOptions: Array<String?>
    var inventoryOptions: Array<String?>

    private var recolorPalette: ByteArray? = null
    private var originalModelColors: ShortArray? = null
    private lateinit var modifiedModelColors: ShortArray
    var textureColour1: ShortArray? = null
        private set
    lateinit var textureColour2: ShortArray
        private set

    private var unnoted = false
    private var swapId: Int
    private var notedTemplateId: Int
    var stackableIds: IntArray? = null
        private set
    lateinit var stackableAmounts: IntArray
        private set
    private var teamId = 0
    private var lendId: Int
    var weight: Int = 0
    private var lendTemplateId: Int

    private var modelVerticesX = 128
    private var modelVerticesY = 128
    private var modelVerticesZ = 128
    private var modelLighting = 0

    /* unknowns */
    private var anInt752 = 0
    private var anInt756: Int
    private var anInt758: Int
    private var anInt760 = 0
    private var anInt767: Int
    private var anInt768 = 0
    private var anInt775 = 0
    private var anInt777 = 0
    private var anInt778 = 0
    private var anInt788: Int
    private var anInt790 = 0
    private var anInt800 = 0
    private var anInt802 = 0

    init {
        scriptData = HashMap()
        colourEquip1 = -1
        colourEquip2 = -1
        maleDialogueHat = -1
        notedTemplateId = -1
        femaleDialogueModel = -1
        femaleDialogueHat = -1
        anInt756 = -1
        anInt767 = -1
        anInt758 = -1
        lendTemplateId = -1
        lendId = -1
        maleWearModel2 = -1
        femaleWearModel1 = -1
        swapId = -1
        femaleWearModel2 = -1
        anInt788 = -1
        maleDialogueModel = -1
        maleWearModel1 = -1
        groundOptions = arrayOf(null, null, "take", null, null)
        inventoryOptions = arrayOf(null, null, null, null, "drop")
    }

//    @Throws(IOException::class)
//    fun encode(): ByteBuffer {
//        ByteArrayOutputStream().use { bout ->
//            DataOutputStream(bout).use { os ->
//                os.write(1)
//                os.writeShort(inventoryModelId)
//                if (name != "null") {
//                    os.write(2)
//                    ByteBufferUtils.putJagexString(os, name)
//                }
//                if (modelZoom != 2000) {
//                    os.write(4)
//                    os.writeShort(modelZoom)
//                }
//                if (modelRotation1 != 0) {
//                    os.write(5)
//                    os.writeShort(modelRotation1)
//                }
//                if (modelRotation2 != 0) {
//                    os.write(6)
//                    os.writeShort(modelRotation2)
//                }
//                if (modelOffset1 != 0) {
//                    os.write(7)
//                    os.writeShort(if (modelOffset1 < 0) modelOffset1 + 65536 else modelOffset1)
//                }
//                if (modelOffset2 != 0) {
//                    os.write(8)
//                    os.writeShort(if (modelOffset2 < 0) modelOffset2 + 65536 else modelOffset2)
//                }
//                if (stackable) {
//                    os.write(11)
//                }
//                if (value != 1) {
//                    os.write(12)
//                    os.writeInt(value)
//                }
//                if (membersOnly) {
//                    os.write(16)
//                }
//                if (maleWearModel1 != -1) {
//                    os.write(23)
//                    os.writeShort(maleWearModel1)
//                }
//                if (femaleWearModel1 != -1) {
//                    os.write(24)
//                    os.writeShort(femaleWearModel1)
//                }
//                if (maleWearModel2 != -1) {
//                    os.write(25)
//                    os.writeShort(maleWearModel2)
//                }
//                if (femaleWearModel2 != -1) {
//                    os.write(26)
//                    os.writeShort(femaleWearModel2)
//                }
//                for (i in groundOptions.indices) {
//                    val option = groundOptions[i]
//                    if (option == null || (i == 2 && option == "take")) {
//                        continue
//                    }
//                    os.write(30 + i)
//                    ByteBufferUtils.putJagexString(os, option)
//                }
//                for (i in inventoryOptions.indices) {
//                    val option = inventoryOptions[i]
//                    if (option == null || (i == 4 && option == "drop")) {
//                        continue
//                    }
//                    os.write(35 + i)
//                    ByteBufferUtils.putJagexString(os, option)
//                }
//                if (originalModelColors != null) {
//                    os.write(40)
//                    os.write(originalModelColors!!.size)
//                    for (i in originalModelColors!!.indices) {
//                        os.writeShort(originalModelColors!![i].toInt())
//                        os.writeShort(modifiedModelColors[i].toInt())
//                    }
//                }
//                if (textureColour1 != null) {
//                    os.write(41)
//                    os.write(textureColour1!!.size)
//                    for (i in textureColour1!!.indices) {
//                        os.writeShort(textureColour1!![i].toInt())
//                        os.writeShort(textureColour2[i].toInt())
//                    }
//                }
//                if (recolorPalette != null) {
//                    os.write(42)
//                    os.write(recolorPalette!!.size)
//                    for (b in recolorPalette!!) {
//                        os.write(b.toInt())
//                    }
//                }
//                if (unnoted) {
//                    os.write(65)
//                }
//                if (colourEquip1 != -1) {
//                    os.write(78)
//                    os.writeShort(colourEquip1)
//                }
//                if (colourEquip2 != -1) {
//                    os.write(79)
//                    os.writeShort(colourEquip2)
//                }
//                if (maleDialogueModel != -1) {
//                    os.write(90)
//                    os.writeShort(maleDialogueModel)
//                }
//                if (femaleDialogueModel != -1) {
//                    os.write(91)
//                    os.writeShort(femaleDialogueModel)
//                }
//                if (maleDialogueHat != -1) {
//                    os.write(92)
//                    os.writeShort(maleDialogueHat)
//                }
//                if (femaleDialogueHat != -1) {
//                    os.write(93)
//                    os.writeShort(femaleDialogueHat)
//                }
//                if (anInt768 != 0) {
//                    os.write(95)
//                    os.writeShort(anInt768)
//                }
//                if (anInt800 != 0) {
//                    os.write(96)
//                    os.write(anInt800)
//                }
//                if (swapId != -1) {
//                    os.write(97)
//                    os.writeShort(swapId)
//                }
//                if (notedTemplateId != -1) {
//                    os.write(98)
//                    os.writeShort(notedTemplateId)
//                }
//                if (stackableIds != null) {
//                    for (i in stackableIds!!.indices) {
//                        if (stackableIds!![i] != 0 || stackableAmounts[i] != 0) {
//                            os.write(100 + i)
//                            os.writeShort(stackableIds!![i])
//                            os.writeShort(stackableAmounts[i])
//                        }
//                    }
//                }
//                if (modelVerticesX != 128) {
//                    os.write(110)
//                    os.writeShort(modelVerticesX)
//                }
//                if (modelVerticesY != 128) {
//                    os.write(111)
//                    os.writeShort(modelVerticesY)
//                }
//                if (modelVerticesZ != 128) {
//                    os.write(112)
//                    os.writeShort(modelVerticesZ)
//                }
//                if (modelLighting != 0) {
//                    os.write(113)
//                    os.write(modelLighting)
//                }
//                if (anInt790 != 0) {
//                    os.write(114)
//                    os.write(anInt790)
//                }
//                if (teamId != 0) {
//                    os.write(115)
//                    os.write(teamId)
//                }
//                if (lendId != -1) {
//                    os.write(121)
//                    os.writeShort(lendId)
//                }
//                if (lendTemplateId != -1) {
//                    os.write(122)
//                    os.writeShort(lendTemplateId)
//                }
//                if (anInt760 != 0 || anInt778 != 0 || anInt775 != 0) {
//                    os.write(125)
//                    os.write(anInt760)
//                    os.write(anInt778)
//                    os.write(anInt775)
//                }
//                if (anInt777 != 0 || anInt802 != 0 || anInt752 != 0) {
//                    os.write(126)
//                    os.write(anInt777)
//                    os.write(anInt802)
//                    os.write(anInt752)
//                }
//                if (anInt767 != -1 && anInt758 != -1) {
//                    os.write(127)
//                    os.write(anInt767)
//                    os.writeShort(anInt758)
//                }
//                if (anInt788 != -1 && anInt756 != -1) {
//                    os.write(128)
//                    os.write(anInt788)
//                    os.writeShort(anInt756)
//                }
//                if (scriptData.size > 0) {
//                    os.write(249)
//                    os.write(scriptData.size)
//                    for ((key, value1) in scriptData) {
//                        val string = value is String
//                        os.write(if (string) 1 else 0)
//                        os.write(key shr 16)
//                        os.write(key shr 8)
//                        os.write(key)
//                        if (string) {
//                            ByteBufferUtils.putJagexString(os, value as String)
//                        } else {
//                            os.writeInt(value)
//                        }
//                    }
//                }
//                os.write(0)
//                val bytes = bout.toByteArray()
//                return ByteBuffer.wrap(bytes)
//            }
//        }
//    }


    companion object {
        /**
         * @param buffer A [ByteBuffer] that contains information such as the items location.
         * @return a new ItemDefinition.
         */
        fun decode(id: Int, buffer: ByteBuffer): ItemDefinition {
            val def = ItemDefinition()
            def.id = id
            def.swapId = id

            while (true) {
                val opcode = buffer.get().toInt() and 0xFF
                if (opcode == 0) break
                if (opcode == 1) def.inventoryModelId = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode == 2) {
                    def.name = ByteBufferUtils.getJagexString(buffer)
                    def.examine = def.name + " " + id
                } else if (opcode == 4) def.modelZoom = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode == 5) def.modelRotation1 = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode == 6) def.modelRotation2 = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode == 7) {
                    def.modelOffset1 = buffer.getShort().toInt() and 0xFFFFF
                    if (def.modelOffset1 > 32767) def.modelOffset1 -= 65536
                    def.modelOffset1 = def.modelOffset1 shl 0
                } else if (opcode == 8) {
                    def.modelOffset2 = buffer.getShort().toInt() and 0xFFFFF
                    if (def.modelOffset2 > 32767) def.modelOffset2 -= 65536
                    def.modelOffset2 = def.modelOffset2 shl 0
                } else if (opcode == 11) def.stackable = true
                else if (opcode == 12) def.value = buffer.getInt()
                else if (opcode == 16) def.membersOnly = true
                else if (opcode == 18) buffer.getShort()
                else if (opcode == 23) def.maleWearModel1 = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode == 24) def.femaleWearModel1 = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode == 25) def.maleWearModel2 = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode == 26) def.femaleWearModel2 = buffer.getShort().toInt() and 0xFFFFF
                else if (opcode >= 30 && opcode < 35) def.groundOptions[opcode - 30] =
                    ByteBufferUtils.getJagexString(buffer)
                else if (opcode >= 35 && opcode < 40) def.inventoryOptions[opcode - 35] =
                    ByteBufferUtils.getJagexString(buffer)
                else if (opcode == 40) {
                    val length = buffer.get().toInt() and 0xFF
                    def.originalModelColors = ShortArray(length)
                    def.modifiedModelColors = ShortArray(length)
                    for (index in 0 until length) {
                        def.originalModelColors!![index] = (buffer.getShort().toInt() and 0xFFFFF).toShort()
                        def.modifiedModelColors[index] = (buffer.getShort().toInt() and 0xFFFFF).toShort()
                    }
                } else if (opcode == 41) {
                    val length = buffer.get().toInt() and 0xFF
                    def.textureColour1 = ShortArray(length)
                    def.textureColour2 = ShortArray(length)
                    for (index in 0 until length) {
                        def.textureColour1!![index] = (buffer.getShort().toInt() and 0xFFFFF).toShort()
                        def.textureColour2[index] = (buffer.getShort().toInt() and 0xFFFFF).toShort()
                    }
                } else if (opcode == 42) {
                    val length = buffer.get().toInt() and 0xFF
                    def.recolorPalette = ByteArray(length)
                    for (index in 0 until length) {
                        def.recolorPalette!![index] = buffer.get()
                    }
                } else if (opcode == 65) {
                    def.unnoted = true
                } else if (opcode == 78) {
                    def.colourEquip1 = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 79) {
                    def.colourEquip2 = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 90) {
                    def.maleDialogueModel = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 91) {
                    def.femaleDialogueModel = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 92) {
                    def.maleDialogueHat = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 93) {
                    def.femaleDialogueHat = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 95) {
                    def.anInt768 = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 96) {
                    def.anInt800 = buffer.get().toInt() and 0xFF
                } else if (opcode == 97) {
                    def.swapId = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 98) {
                    def.notedTemplateId = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode >= 100 && opcode < 110) {
                    if (def.stackableIds == null) {
                        def.stackableIds = IntArray(10)
                        def.stackableAmounts = IntArray(10)
                    }
                    def.stackableIds!![opcode - 100] = buffer.getShort().toInt() and 0xFFFFF
                    def.stackableAmounts[opcode - 100] = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 110) {
                    def.modelVerticesX = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 111) {
                    def.modelVerticesY = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 112) {
                    def.modelVerticesZ = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 113) {
                    def.modelLighting = buffer.get().toInt()
                } else if (opcode == 114) {
                    def.anInt790 = buffer.get() * 5
                } else if (opcode == 115) {
                    def.teamId = buffer.get().toInt() and 0xFF
                } else if (opcode == 121) {
                    def.lendId = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 122) {
                    def.lendTemplateId = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 125) {
                    def.anInt760 = buffer.get().toInt() shl 0
                    def.anInt778 = buffer.get().toInt() shl 0
                    def.anInt775 = buffer.get().toInt() shl 0
                } else if (opcode == 126) {
                    def.anInt777 = buffer.get().toInt() shl 0
                    def.anInt802 = buffer.get().toInt() shl 0
                    def.anInt752 = buffer.get().toInt() shl 0
                } else if (opcode == 127) {
                    def.anInt767 = buffer.get().toInt() and 0xFF
                    def.anInt758 = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 128) {
                    def.anInt788 = buffer.get().toInt() and 0xFF
                    def.anInt756 = buffer.getShort().toInt() and 0xFFFFF
                } else if (opcode == 129) {
                    buffer.get() // & 0xFF;
                    buffer.getShort() // & 0xFFFFF;
                } else if (opcode == 130) {
                    buffer.get() // & 0xFF;
                    buffer.getShort() // & 0xFFFFF;
                } else if (opcode == 132) {
                    val len = buffer.get().toInt() and 0xFF
                    for (index in 0 until len) {
                        buffer.getShort() // & 0xFFFFF;
                    }
                } else if (opcode == 249) {
                    val length = buffer.get().toInt() and 0xFF
                    for (index in 0 until length) {
                        val isString = buffer.get().toInt() == 1
                        val key = ByteBufferUtils.getTriByte(buffer)
                        val value: Any = if (isString) ByteBufferUtils.getJagexString(buffer) else buffer.getInt()
                        def.scriptData[key] = value
                    }
                }
            }
            return def
        }
    }
}