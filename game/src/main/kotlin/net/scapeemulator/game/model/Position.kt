package net.scapeemulator.game.model

import net.scapeemulator.game.pathfinder.RegionManager

data class Position(val x: Int, val y: Int, val height: Int = 0) {

    val regionId: Int get() = (x shr 6) shl 8 or (y shr 6)
    private val regionX: Int get() = x shr 3
    private val regionY: Int get() = y shr 3

    val centralRegionX: Int get() = x / 8
    val centralRegionY: Int get() = y / 8

    // script message
    val packed: Int get() = (height shl 28) or (x shl 14) or y

    fun getChunkBase() = Position(regionX shl 3, regionY shl 3, height)

    fun getChunkOffsetX(): Int {
        val x = x - ((x shr 6) shl 6) //localX
        return x - ((x / RegionManager.SIZE) * RegionManager.SIZE)
    }

    fun getChunkOffsetY(): Int {
        val y = y - ((y shr 6) shl 6) //localY
        return y - ((y / RegionManager.SIZE) * RegionManager.SIZE)
    }

    //    fun getChunkBase(): Position = Position(getRegionX() shl 3, getRegionY() shl 3, z)
    //    fun getLocalX(): Int = x - ((x shr 6) shl 6)
//    fun getLocalY(): Int = y - ((y shr 6) shl 6)
//    fun getSceneX(): Int = x - ((getRegionX() - 6) shl 3)
//    fun getSceneY(): Int = y - ((getRegionY() - 6) shl 3)
    fun getRegionX(position: Position): Int = x - ((position.regionX - 6) shl 3)
    fun getRegionY(position: Position): Int = y - ((position.regionY - 6) * 8)
    fun getLocalX(centralRegionX: Int): Int = x - ((centralRegionX - 6) * 8)
    fun getLocalY(centralRegionY: Int): Int = y - ((centralRegionY - 6) * 8)

//    fun isWithinDistance(position: Position): Boolean = isWithinDistanceArea(position, 15)

    fun isWithinDistance(position: Position, distance: Int = 15): Boolean {
        if (height != position.height) return false
        val deltaX: Int = position.x - x
        val deltaY: Int = position.y - y
        return deltaX >= -(distance + 1) && deltaX <= distance && deltaY >= -(distance + 1) && deltaY <= distance
    }

    fun isWithinTileDistance(position: Position, dist: Int): Boolean {
        if (position.height != height) return false
        val deltaX: Int = position.x - x
        val deltaY: Int = position.y - y
        return deltaX <= dist && deltaX >= -dist && deltaY <= dist && deltaY >= -dist
    }

    fun isWithinScene(position: Position): Boolean {
//        if (height != position.height) return false
//        val deltaX: Int = position.x - x
//        val deltaY: Int = position.y - y
//        return deltaX >= -52 && deltaX <= 51 && deltaY >= -52 && deltaY <= 51
        return isWithinDistance(position, 51)
    }

    fun blockHash(): Int = (x and 0x7) shl 4 or (y and 0x7)
    fun blockHash2(): Int = (x and 0x7) shl 4 or (y and 0x7)

    /* pathfinder todo cleanup*/
//    fun getLocalX(): Int = getLocalX(centralRegionX)
//    fun getLocalY(): Int = getLocalY(centralRegionY)
    fun getLocalX() = x - ((x shr 6) shl 6)
    fun getLocalY() = y - ((y shr 6) shl 6)

    fun getBaseLocalX(): Int = getBaseLocalX(centralRegionX)
    fun getBaseLocalY(): Int = getBaseLocalY(centralRegionY)
    private fun getBaseLocalX(centralRegionX: Int): Int = (centralRegionX - 6) * 8
    private fun getBaseLocalY(centralRegionY: Int): Int = (centralRegionY - 6) * 8

    /*autogen*/
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

    override fun toString(): String = "Position(x=$x, y=$y, height=$height)"

}

fun main() {
    val position = Position(3222, 3222, 0)
    position.apply {
        val localX = x - ((x shr 6) shl 6)
        val localY = y - ((y shr 6) shl 6)

        println("LocalX: " + getLocalX())
        println("LocalY: " + getLocalY())

        println("getRegionX: " + getRegionX(position))
        println("getRegionY: " + getRegionY(position))

        println(localX)
        println(localY)

        println("ChunkOffsetX: " + getChunkOffsetX())
        println("ChunkOffsetY: " + getChunkOffsetY())

        println(getBaseLocalX())
        println(getBaseLocalY())
    }
}