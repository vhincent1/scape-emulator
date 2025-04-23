package net.scapeemulator.game.msg

class ScriptMessage(val id: Int, val types: String, vararg parameters: Any) : Message() {

    val parameters: Array<Any> = arrayOf(parameters)
}
