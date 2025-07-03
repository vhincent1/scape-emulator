package net.scapeemulator.game.model

/**
 * Represents a region constructed of <tt>13 x 13 x 4</tt> individual <tt>8 x 8</tt> map tiles taken
 * from other parts of the world.
 *
 * @author Graham
 * @author David Insley
 */
class RegionPalette {
    private val tiles = Array(4) { Array(13) { arrayOfNulls<Tile>(13) } }

    fun setTile(height: Int, x: Int, y: Int, tile: Tile?) {
        tiles[height][x][y] = tile
    }

    fun getHash(height: Int, x: Int, y: Int): Int = if (tiles[height][x][y] != null) tiles[height][x][y]!!.hash else -1

    class Tile(x: Int, y: Int, height: Int = 0, rotation: Rotation = Rotation.NONE) {
        enum class Rotation(val id: Int) {
            NONE(0), CW_90(1), CW_180(2), CW_270(3);

            fun rotate(rotate: Rotation): Rotation = forId(rotate.id + id)

            companion object {
                fun forId(id: Int): Rotation {
                    var id = id
                    id %= 4
                    for (rotation in entries) if (rotation.id == id) return rotation
                    throw RuntimeException()
                }
            }
        }

        internal val hash: Int = (x / 8) shl 14 or ((y / 8) shl 3) or ((height % 4) shl 24) or ((rotation.id % 4) shl 1)

        constructor(position: Position) : this(position.x, position.y, position.height, Rotation.NONE)
        constructor(position: Position, rotation: Rotation) : this(position.x, position.y, position.height, rotation)
    }
}