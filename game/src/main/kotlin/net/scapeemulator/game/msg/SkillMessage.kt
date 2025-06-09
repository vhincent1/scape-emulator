package net.scapeemulator.game.msg

data class SkillMessage(@JvmField val skill: Int, @JvmField val level: Int, @JvmField val experience: Int) : Message
