package net.scapeemulator.game.model

typealias RunScriptType = RunScript.Type
class RunScript(val block: (Any) -> Unit) {
    enum class Type(val id: Int, val types: String) {
        INT(108, "s"),
        STRING(109, "s")
    }
}