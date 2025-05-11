package net.scapeemulator.game.msg

class ScriptMessage(val id: Int, val types: String, vararg val parameters: Any) : Message()