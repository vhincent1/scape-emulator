package net.scapeemulator.game.net.game

enum class DataType(@JvmField val bytes: Int) {
    BYTE(1), SHORT(2), TRI_BYTE(3), INT(4), LONG(8)
}
