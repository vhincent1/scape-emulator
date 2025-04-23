package net.scapeemulator.game.model

class Position {
    val x: Int
    val y: Int
    val height: Int

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
        this.height = 0
    }

    constructor(x: Int, y: Int, height: Int) {
        this.x = x
        this.y = y
        this.height = height
    }

    fun getLocalX(centralRegionX: Int): Int {
        return x - ((centralRegionX - 6) * 8)
    }

    fun getLocalY(centralRegionY: Int): Int {
        return y - ((centralRegionY - 6) * 8)
    }

    val centralRegionX: Int
        get() = x / 8

    val centralRegionY: Int
        get() = y / 8

    fun isWithinDistance(position: Position): Boolean {
        val deltaX = position.x - x
        val deltaY = position.y - y
        return deltaX >= -16 && deltaX <= 15 && deltaY >= -16 && deltaY <= 15
    }

    fun toPackedInt(): Int {
        return (height shl 28) or (x shl 14) or y
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + height
        result = prime * result + x
        result = prime * result + y
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val obj2 = other as Position
        if (height != obj2.height) return false
        if (x != obj2.x) return false
        if (y != obj2.y) return false
        return true
    }
}
