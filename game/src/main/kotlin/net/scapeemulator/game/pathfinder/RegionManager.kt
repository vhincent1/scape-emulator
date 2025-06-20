/**
 * scape-emulator-final
 * Copyright (c) 2014 Justin Conner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in  the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http:></http:>//www.gnu.org/license/>.
 */
package net.scapeemulator.game.pathfinder

import net.scapeemulator.game.model.GameObject
import net.scapeemulator.game.model.Position
import java.util.*

class RegionManager2 {

    companion object {
        /**
         * The size of one side of the region array.
         */
        const val SIZE: Int = 256
    }

    val regions = arrayOfNulls<Region>(SIZE * SIZE)

    fun forId(id: Int): Region? {
//        val region = REGION_CACHE[id]
//        val x = id shr 8 and 0xFF
//        val y = id and 0xFF
//        if (region == null) REGION_CACHE[id] = Region(x, y)

        return regions[id]
    }

    class Region(val x: Int, val y: Int, val plane: Int = 0) {
        companion object {
            const val SIZE: Int = 256
            const val REGION_SIZE: Int = 64
            const val MAXIMUM_PLANE: Int = 4
        }

        /**
         * The flags within the region.
         */
        private val tiles: Array<Array<Tile?>>

        /**
         * The objects within the region.
         */
        private val objects: MutableMap<Position, GameObject> = HashMap()

        /**
         * Constructs a new [Region];
         */
        init {
            this.tiles = Array(MAXIMUM_PLANE) {
                arrayOfNulls(REGION_SIZE * REGION_SIZE)
            }
            for (i in 0 until MAXIMUM_PLANE) {
                for (j in 0 until REGION_SIZE * REGION_SIZE) {
                    tiles[i][j] = Tile()
                }
            }
        }

        fun regionId(): Int {
            val regionId = ((x shr 6) shl 8) or (y shr 6)
            return regionId
        }

        fun getTile(): Tile? {
            val localX = x and 0x3f
            val localY = y and 0x3f
            return tiles[plane][localX + localY * REGION_SIZE]
        }

        fun getTile(plane: Int, x: Int, y: Int): Tile? = tiles[plane][x + y * REGION_SIZE]

        fun addObject(gameObj: GameObject) {
            objects[gameObj.position] = gameObj
        }

        fun getObject(position: Position): GameObject? = objects[position]
        fun getObjects(): Collection<GameObject> = Collections.unmodifiableCollection(objects.values)
        override fun toString(): String = "Region(id=${regionId()} x=$x, y=$y, plane=$plane)"
    }

    class RegionFlags {

        //        val members: Boolean = false
//        val baseX: Int = 0
//        val baseY: Int = 0
//        private val clippingFlags: Array<IntArray>
//        private val landscape: Array<BooleanArray>
//        private val projectile = false
        constructor(plane: Int, x: Int, y: Int)
    }

    class RegionChunk {
        //8x8
        companion object {
            const val SIZE = 8
        }
        //position, base, currentBase
        //regionPlane
        //list GroundItems
        //list Objects
        //scenery[][] objects
        //flags(20)
    }

    fun getRegion(x: Int, y: Int): Region? {
        /* Calculate the region coordinates */
        val regionId = ((x shr 6) shl 8) or (y shr 6)
        val regionX = x shr 6
        val regionY = y shr 6
        return regions[regionId]
    }

    fun getRegion(position: Position): Region? {
        val id = ((position.x shr 6) shl 8) or (position.y shr 6)
        return regions[id]
    }

    /**
     * Initializes the region at the specified coordinates.
     */
    fun initializeRegion(position: Position) {
        val regionId = ((position.x shr 6) shl 8) or (position.y shr 6)
//        val x = regionId shr 8 and 0xFF
//        val y = regionId and 0xFF

        /* Calculate the coordinates */
        val regionX = position.x shr 6
        val regionY = position.y shr 6
        regions[regionId] = Region(regionX, regionY)
    }

    /**
     * Gets if the set contains a region for the specified coordinates.
     */
    fun isRegionInitialized(position: Position): Boolean {
        /* Calculate the coordinates */
        val regionX = position.x shr 6
        val regionY = position.y shr 6

        /* Get if the region is not null */
        return getRegion(position) != null
    }

    fun isTeleportPermitted(position: Position): Boolean {
        val localX = position.x and 0x3f
        val localY = position.y and 0x3f
        val flag = getRegion(position)?.getTile(position.height, localX, localY)?.flags() ?: return false
        return (flag and 0x12c0102) == 0 || ((flag and 0x12c0108) == 0) || ((flag and 0x12c0120) == 0) || ((flag and 0x12c0180) == 0)
    }

//    fun isTeleportPermitted(z: Int, x: Int, y: Int): Boolean {
//        if (!isLandscape(z, x, y)) {
//            return false
//        }
//        val flag: Int = getClippingFlag(z, x, y)
//        return (flag and 0x12c0102) == 0 || ((flag and 0x12c0108) == 0) || ((flag and 0x12c0120) == 0) || ((flag and 0x12c0180) == 0)
//    }
}

class Region {
    /**
     * The flags within the region.
     */
    private val tiles: Array<Array<Tile?>>

    /**
     * The objects within the region.
     */
    private val objects: MutableMap<Position, GameObject>

    /**
     * Constructs a new [net.scapeemulator.game.model.region.Region];
     */
    init {
        this.objects = HashMap<Position, GameObject>()

        this.tiles = Array(MAXIMUM_PLANE) {
            arrayOfNulls(
                REGION_SIZE * REGION_SIZE
            )
        }
        for (i in 0 until MAXIMUM_PLANE) {
            for (j in 0 until REGION_SIZE * REGION_SIZE) {
                tiles[i][j] = Tile()
            }
        }
    }

    fun getTile(plane: Int, x: Int, y: Int): Tile? {
        return tiles[plane][x + y * REGION_SIZE]
    }

    fun addObject(`object`: GameObject) {
        objects[`object`.position] = `object`
    }

    fun getObject(position: Position): GameObject? {
        return objects[position]
    }

    fun getObjects(): Collection<GameObject> {
        return Collections.unmodifiableCollection<GameObject>(objects.values)
    }

    companion object {
        const val REGION_SIZE: Int = 64

        /**
         * The maximum plane.
         */
        const val MAXIMUM_PLANE: Int = 4
    }
}

class RegionManager {
    /**
     * The regions for the traversal data.
     */
    private val regions: Array<Region?> =
        arrayOfNulls<Region>(
            SIZE * SIZE
        )

    fun getRegion(position: Position): Region? {
        return getRegion(position.x, position.y)
    }

    fun getRegion(x: Int, y: Int): Region? {
        return regions[(x shr 6) + (y shr 6) * SIZE]
    }

    /**
     * Initializes the region at the specified coordinates.
     */
    fun initializeRegion(position: Position) {
        /* Calculate the coordinates */
        val regionX: Int = position.x shr 6
        val regionY: Int = position.y shr 6

        regions[regionX + regionY * SIZE] = Region()
    }

    /**
     * Gets if the set contains a region for the specified coordinates.
     */
    fun isRegionInitialized(position: Position): Boolean {
        /* Calculate the coordinates */
        val regionX: Int = position.x shr 6
        val regionY: Int = position.y shr 6

        /* Get if the region is not null */
        return regions[regionX + regionY * SIZE] != null
    }

    companion object {
        /**
         * The size of one side of the region array.
         */
        const val SIZE: Int = 256
    }
}
