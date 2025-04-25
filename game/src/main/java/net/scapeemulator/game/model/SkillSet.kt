package net.scapeemulator.game.model

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class SkillSet {
    private val level = IntArray(24)
    private val exp = DoubleArray(level.size)
    private val listeners: MutableList<SkillListener> = ArrayList()

    init {
        for (i in level.indices) {
            level[i] = 1
            exp[i] = 0.0
        }

        level[Skill.HITPOINTS] = 10
        exp[Skill.HITPOINTS] = 1184.0
    }

    fun addListener(listener: SkillListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: SkillListener) {
        listeners.remove(listener)
    }

    fun removeListeners() {
        listeners.clear()
    }

    fun size(): Int {
        return level.size
    }

    fun getCurrentLevel(skill: Int): Int {
        return level[skill]
    }

    fun getMaximumLevel(skill: Int): Int {
        return getLevelFromExperience(exp[skill])
    }

    fun getExperience(skill: Int): Double {
        return exp[skill]
    }

    fun addExperience(skill: Int, xp: Double) {
        require(!(xp < 0)) { "Experience cannot be negative." }

        val oldLevel = getMaximumLevel(skill)
        exp[skill] = min(exp[skill] + xp, MAXIMUM_EXPERIENCE)

        val delta = getMaximumLevel(skill) - oldLevel
        level[skill] += delta

        for (listener in listeners) listener.skillChanged(this, skill)

        if (delta > 0) {
            for (listener in listeners) listener.skillLevelledUp(this, skill)
        }
    }

    fun refresh() {
        for (skill in level.indices) {
            for (listener in listeners) listener.skillChanged(this, skill)
        }
    }

    val combatLevel: Int
        get() {
            val defence = getMaximumLevel(Skill.DEFENCE)
            val hitpoints = getMaximumLevel(Skill.HITPOINTS)
            val prayer = getMaximumLevel(Skill.PRAYER)
            val attack = getMaximumLevel(Skill.ATTACK)
            val strength = getMaximumLevel(Skill.STRENGTH)
            val magic = getMaximumLevel(Skill.MAGIC)
            val ranged = getMaximumLevel(Skill.RANGED)
            val summoning = getMaximumLevel(Skill.SUMMONING) // TODO set to zero on an F2P world

            val base = 1.3 * max(max((attack + strength).toDouble(), 1.5 * magic), 1.5 * ranged)

            return floor(
                (defence + hitpoints + floor(prayer / 2.0) +
                        floor(summoning / 2.0) + base) / 4.0
            ).toInt()
        }

    val totalLevel: Int
        get() {
            var total = 0
            for (skill in level.indices) total += getMaximumLevel(skill)
            return total
        }

    companion object {
        const val MAXIMUM_EXPERIENCE: Double = 200000000.0

        private fun getLevelFromExperience(xp: Double): Int {
            var points = 0
            var output: Int

            for (level in 1..99) {
                points = (points + floor(level + 300.0 * 2.0.pow(level / 7.0))).toInt()
                output = points / 4

                if ((output - 1) >= xp) return level
            }

            return 99
        }
    }
}
