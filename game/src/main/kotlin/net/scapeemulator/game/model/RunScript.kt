package net.scapeemulator.game.model

class RunScript(val block: (Player, Any) -> Unit) {
    enum class Type(val id: Int, val types: String) {
        INT(108, "s"),
        STRING(109, "s")
    }
}