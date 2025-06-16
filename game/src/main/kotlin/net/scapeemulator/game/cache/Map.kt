package net.scapeemulator.game.cache

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.pathfinder.MapListener
import net.scapeemulator.game.pathfinder.Tile
import java.nio.ByteBuffer

class Map(val x: Int, val y: Int, private val tiles: Array<Array<Array<Tile>>>) {
    class Tile2 {
        // peterbjornx's refactor suggests a remove roof flag, but is wrong ?
        //public static final int FLAG_ = 0x4; ?
        //public static final int FLAG_ = 0x8; zero logic height ?
        //public static final int FLAG_ = 0x10; seems to make it ignored ?
        var height: Int = 0
        var overlay: Int = 0
        var underlay: Int = 0
        var shape: Int = 0
        var shapeRotation: Int = 0
        var flags: Int = 0

        //todo member zones see @arios @MapZone @ZoneRestriction
        companion object {
            const val FLAG_CLIP: Int = 0x1
            const val FLAG_BRIDGE: Int = 0x2

            // no followers
            const val FLAG_FOLLOWERS = 0 shl 1

            // no random events
            const val FLAG_RANDOM_EVENT = 1 shl 2

            // no fires
            const val FLAG_FIRES = 1 shl 2

            // members only
            const val FLAG_MEMBERS = 1 shl 3

            // no cannon
            const val FLAG_CANNON = 1 shl 4
        }
    }

    fun getTile(x: Int, y: Int, plane: Int): Tile {
        return tiles[x][y][plane]
    }

    companion object {
        @JvmStatic
        fun decode(listeners: List<MapListener>, x: Int, y: Int, buffer: ByteBuffer): Map {
            val tiles = Array(64) {
                Array(64) {
                    @Suppress("UNCHECKED_CAST")
                    arrayOfNulls<Tile>(4) as Array<Tile>
                }
            }
            for (plane in 0..3) {
                for (localX in 0..63) {
                    for (localY in 0..63) {
                        val position = Position(x * 64 + localX, y * 64 + localY, plane);
                        tiles[localX][localY][plane] =
                            decodeTile(listeners, tiles, localX, localY, plane, position, buffer)
                    }
                }
            }
            return Map(x, y, tiles)
        }

        private fun decodeTile(
            listeners: List<MapListener>,
            tiles: Array<Array<Array<Tile>>>, x: Int, y: Int, plane: Int, position: Position, buffer: ByteBuffer
        ): Tile {
            val tile = Tile()
            val flags = 0
            while (true) {
                val config = buffer.get().toInt() and 0xFF
                if (config == 0) {
//                    if (plane == 0)
//                        tile.height = -tileHeight(x + 932731, y + 556238) * 8
//                    else
//                        tile.height = tiles[x][y][plane - 1].height - 240

                    listeners.forEach { it.tileDecoded(flags, position) }

                    return tile
                } else if (config == 1) {
                    var height = buffer.get().toInt() and 0xFF
                    if (height == 1) height = 0

//                    if (plane == 0)
//                        tile.height = -height * 8
//                    else
//                        tile.height = tiles[x][y][plane - 1].height - height * 8

                    listeners.forEach { it.tileDecoded(flags, position) }
                    return tile
                } else if (config <= 49) {
//                    tile.overlay = buffer.get().toInt() and 0xFF
//                    tile.shape = (config - 2) / 4
//                    tile.shapeRotation = (config - 2) % 4
                    val overlay = buffer.get().toInt() and 0xFF
                    val shape = (config - 2) / 4
                    val shapeRotation = (config - 2) % 4
                } else if (config <= 81) {
                    tile.flags = config - 49
                } else {
                    val underlay = config - 81
                }
            }
        }
    }
}
