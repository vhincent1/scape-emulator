package net.scapeemulator.game.model

typealias HitType = Hit.Type

class Hit(val damage: Int, val type: Type) {
    enum class Type {
        NONE,
        NORMAL,
        POISON,
        DISEASE
    }
}

