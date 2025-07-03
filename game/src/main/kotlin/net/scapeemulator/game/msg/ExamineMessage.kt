package net.scapeemulator.game.msg

typealias ExamineType = ExamineMessage.Type

data class ExamineMessage(val id: Int, val type: Type) : Message {
    enum class Type { ITEM, NPC, OBJECT }
}