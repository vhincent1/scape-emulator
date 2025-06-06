package net.scapeemulator.game.model

import net.scapeemulator.game.pathfinder.RegionManager

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

    fun getChunkBase() = Position(getRegionX() shl 3, getRegionY() shl 3, height)

    fun getChunkOffsetX(): Int {
        val x = x - ((x shr 6) shl 6)
        return x - ((x / RegionManager.RegionChunk.SIZE) * RegionManager.RegionChunk.SIZE)
    }

    fun getChunkOffsetY(): Int {
        val y = y - ((y shr 6) shl 6)
        return y - ((y / RegionManager.RegionChunk.SIZE) * RegionManager.RegionChunk.SIZE)
    }

    //    fun getChunkBase(): Position = Position(getRegionX() shl 3, getRegionY() shl 3, z)
    fun getRegionX(): Int = x shr 3
    fun getRegionY(): Int = y shr 3

    //    fun getLocalX(): Int = x - ((x shr 6) shl 6)
//    fun getLocalY(): Int = y - ((y shr 6) shl 6)
//    fun getSceneX(): Int = x - ((getRegionX() - 6) shl 3)
//    fun getSceneY(): Int = y - ((getRegionY() - 6) shl 3)
    fun getSceneX(pos: Position): Int = x - ((pos.getRegionX() - 6) shl 3)
    fun getSceneY(pos: Position): Int = y - ((pos.getRegionY() - 6) * 8)
    fun getRegionId(): Int = (x shr 6) shl 8 or (y shr 6)

    fun getLocalX(centralRegionX: Int): Int = x - ((centralRegionX - 6) * 8)
    fun getLocalY(centralRegionY: Int): Int = y - ((centralRegionY - 6) * 8)

    val centralRegionX: Int get() = x / 8
    val centralRegionY: Int get() = y / 8

    fun isWithinDistance(position: Position): Boolean = isWithinDistanceArea(position, 15)

    fun isWithinDistanceArea(position: Position, distance: Int): Boolean {
        val deltaX: Int = position.x - x
        val deltaY: Int = position.y - y
        return deltaX >= -(distance + 1) && deltaX <= distance && deltaY >= -(distance + 1) && deltaY <= distance
    }

    fun isWithinDistance(position: Position, dist: Int): Boolean {
        if (position.height != height) return false
        val deltaX: Int = position.x - x
        val deltaY: Int = position.y - y
        return deltaX <= dist && deltaX >= -dist && deltaY <= dist && deltaY >= -dist
    }

    // script message
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

    /* pathfinder */

    fun getLocalX(): Int {
        return getLocalX(centralRegionX)
    }

    fun getLocalY(): Int {
        return getLocalY(centralRegionY)
    }

    fun getBaseLocalX(): Int {
        return getBaseLocalX(centralRegionX)
    }

    fun getBaseLocalY(): Int {
        return getBaseLocalY(centralRegionY)
    }

    fun getBaseLocalX(centralRegionX: Int): Int {
        return (centralRegionX - 6) * 8
    }

    fun getBaseLocalY(centralRegionY: Int): Int {
        return (centralRegionY - 6) * 8
    }
}
