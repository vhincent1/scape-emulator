package net.scapeemulator.cache.def

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import net.scapeemulator.cache.util.ByteBufferUtils
import java.nio.ByteBuffer


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class NPCDefinition {
    var id: Int = 0
    var name: String? = null
    var examine: String? = null
    var combatLevel = 0
    var options: List<String>? = null
    var size: Int = 0

    var stance = 0

    //    var config: HashMap<String, Any>? = null
    var respawn: Int? = null
    var weakness: String? = null

    var aggressive: Boolean? = null
    var aggressiveRadius: Int? = null

    var attackLevel: Int? = null
    var defenceLevel: Int? = null
    var lifepoints: Int? = null
    var magicLevel: Int? = null
    var rangeLevel: Int? = null
    var strengthLevel: Int? = null
    var bonuses: ArrayList<Int>? = null//IntArray(14)
    var combatStyle: String? = null
    var protectStyle: Int? = null

    var poisonous: Boolean? = null
    var poisonImmune: Boolean? = null
    var poisonAmount: Int? = null

    var clueLevel: String? = null
    var slayerExp: Double? = null
    var slayerTask: String? = null

    /* combat anim */
    var meleeAnimation: Int? = null
    var defenceAnimation: Int? = null
    var rangeAnimation: Int? = null
    var magicAnimation: Int? = null
    var deathAnimation: Int? = null
    var spawnAnimation: Int? = null
    var spellId: Int? = null

    var combatAudio: ArrayList<Int>? = null

    /* projectiles */
    var projectile: Int? = null
    var projectileHeight: Int? = null
    var startHeight: Int? = null
    var endHeight: Int? = null
    var startGFX: Int? = null
    var endGFX: Int? = null
    var deathGFX: Int? = null

    var movementRadius: Int? = null
    var safespot: Boolean? = null
    var waterNpc: Boolean? = null

    override fun toString(): String {
        return "$name (id: $id, lvl: $combatLevel)"
    }

    companion object {
        // TODO more projectile info needed?
        @Suppress("unused")
        fun decode(id: Int, buffer: ByteBuffer): NPCDefinition {
            val def = NPCDefinition()
            def.id = id
            //def.options = //arrayOfNulls(5)
            def.size = 1
            while (true) {
                val opcode = buffer.get().toInt() and 0xFF
                if (opcode == 0) {
                    break
                }
                if (opcode == 1) {
                    val models = IntArray(buffer.get().toInt() and 0xFF)
                    for (i in models.indices) {
                        var modelId = buffer.getShort().toInt() and 0xffff
                        if (modelId == 65535) {
                            modelId = -1
                        }
                        models[i] = modelId
                    }
                }
                if (opcode == 2) {
                    def.name = ByteBufferUtils.getString(buffer)
                }
                if (opcode == 12) {
                    def.size = buffer.get().toInt() and 0xff
                }
                if (opcode == 13) {
                    var standAnimation = buffer.getShort().toInt()
                }
                if (opcode == 14) {
                    var walkAnimation = buffer.getShort().toInt()
                }
                if (opcode == 15) {
                    var turnAnimation = buffer.getShort().toInt()
                }
                if (opcode == 16) {
                    buffer.getShort()
                } // Another turn animation
                if (opcode == 17) {
                    var walkAnimation = buffer.getShort().toInt()
                    var turn180Animation = buffer.getShort().toInt()
                    var turnCWAnimation = buffer.getShort().toInt()
                    var turnCCWAnimation = buffer.getShort().toInt()
                }
                if (opcode >= 30 && opcode <= 35) {
                    val options: Array<String?> = arrayOfNulls(5)
                    options[opcode - 30] = ByteBufferUtils.getString(buffer)
                    if (options[opcode - 30] == "hidden") {
                        options[opcode - 30] = null
                    }
                }
                if (opcode == 40) {
                    val var0 = buffer.get().toInt() and 0xff
                    for (i in 0 until var0) {
                        val var1 = buffer.getShort().toInt() and 0xffff
                        val var2 = buffer.getShort().toInt() and 0xffff
                    }
                }
                if (opcode == 41) {
                    val var0 = buffer.get().toInt() and 0xff
                    for (i in 0 until var0) {
                        val var1 = buffer.getShort().toInt() and 0xffff
                        val var2 = buffer.getShort().toInt() and 0xffff
                    }
                }
                if (opcode == 42) {
                    val var0 = buffer.get().toInt() and 0xff
                    for (i in 0 until var0) {
                        val var1 = buffer.get().toInt()
                    }
                }
                if (opcode == 60) {
                    val var0 = buffer.get().toInt() and 0xff
                    for (i in 0 until var0) {
                        val var1 = buffer.getShort().toInt() and 0xffff
                    }
                }
                if (opcode == 93) {
                    var isVisibleOnMap = false
                }
                if (opcode == 95) {
                    def.combatLevel = buffer.getShort().toInt() and 0xffff
                }
                if (opcode == 97) {
                    val var0 = buffer.getShort().toInt() and 0xffff // Scale x
                }
                if (opcode == 98) {
                    val var0 = buffer.getShort().toInt() and 0xffff // scale y
                }
                if (opcode == 100) {
                    val var0 = buffer.get().toInt() and 0xff
                }
                if (opcode == 101) {
                    val var0 = buffer.get().toInt() and 0xff
                }
                if (opcode == 102) {
                    val var0 = buffer.getShort().toInt() and 0xffff // todo Head icon
                }
                if (opcode == 103) {
                    val var0 = buffer.getShort().toInt() and 0xffff
                }
                if (opcode == 106 || opcode == 118) {
                    val var0 = buffer.getShort().toInt() and 0xffff
                    val var1 = buffer.getShort().toInt() and 0xffff
                    if (opcode == 118) {
                        val var3 = buffer.getShort().toInt() and 0xffff
                    }

                    val var4 = buffer.get().toInt() and 0xff
                    for (i in 0..var4) {
                        val var5 = buffer.getShort().toInt() and 0xffff
                    }
                }
                if (opcode == 113) {
                    val var0 = buffer.getShort().toInt() and 0xffff
                    val var1 = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 114) {
                    val var0 = buffer.get().toInt() and 0xff
                    val var1 = buffer.get().toInt() and 0xff
                }

                if (opcode == 115) {
                    val var0 = buffer.get().toInt() and 0xff
                    val var1 = buffer.get().toInt() and 0xff
                }

                if (opcode == 119) {
                    val var0 = buffer.get().toInt() and 0xff
                }

                if (opcode == 121) {
                    val var0 = buffer.get().toInt() and 0xff
                    for (i in 0 until var0) {
                        val var1 = buffer.get().toInt() and 0xff
                        val var2 = buffer.get().toInt() and 0xff
                        val var3 = buffer.get().toInt() and 0xff
                        val var4 = buffer.get().toInt() and 0xff
                    }
                }

                if (opcode == 122) {
                    val var0 = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 123) {
                    val var0 = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 125) {
                    val var0 = buffer.get().toInt()
                }

                if (opcode == 126) {
                    val renderAnimationId = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 127) {
                    def.stance = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 128) {
                    val var0 = buffer.get().toInt() and 0xff
                }
                if (opcode == 134) {
                    val var0 = buffer.getShort().toInt() and 0xffff
                    val var1 = buffer.getShort().toInt() and 0xffff
                    val var2 = buffer.getShort().toInt() and 0xffff
                    val var3 = buffer.getShort().toInt() and 0xffff
                    val var4 = buffer.get().toInt() and 0xff
                }

                if (opcode == 135) {
                    val var0 = buffer.get().toInt() and 0xff
                    val var1 = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 136) {
                    val var0 = buffer.get().toInt() and 0xff
                    val var1 = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 137) {
                    val var0 = buffer.getShort().toInt() and 0xffff
                }

                if (opcode == 249) {
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