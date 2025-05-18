package net.scapeemulator.game.model

class Animation(val id: Int, val delay: Int = 0) {
    enum class Priority {
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH
    }
}

