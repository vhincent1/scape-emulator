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

class RegionManager {

    companion object {
        /**
         * The size of one side of the region array.
         */
        const val SIZE: Int = 256
    }

    val REGION_CACHE: MutableMap<Int, Region> = HashMap()

    fun forId(id: Int): Region? {
        val region = REGION_CACHE[id]
        if (region == null) REGION_CACHE[id] = Region(id shr 8 and 0xFF, id and 0xFF)
        return region
    }

    class Region(val x: Int, val y: Int) {
//        companion object {
//            const val SIZE = 64
//            //val x = regionId shr 8 and 0xFF
//            //val y = regionId and 0xFF
//        }
////        val planes =
        /**
         * The flags within the region.
         */
        private val tiles: Array<Array<Tile?>>

        /**
         * The objects within the region.
         */
        private val objects: MutableMap<Position, GameObject> = java.util.HashMap()

        /**
         * Constructs a new [net.scapeemulator.game.model.region.Region];
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

        fun getTile(plane: Int, x: Int, y: Int): Tile? {
            return tiles[plane][x + y * REGION_SIZE]
        }

        fun addObject(gameObj: GameObject) {
            objects[gameObj.position] = gameObj
        }

        fun getObject(position: Position): GameObject? {
            return objects[position]
        }

        fun getObjects(): Collection<GameObject> {
            return Collections.unmodifiableCollection(objects.values)
        }

        companion object {
            const val REGION_SIZE: Int = 64

            /**
             * The maximum plane.
             */
            const val MAXIMUM_PLANE: Int = 4
        }
    }

//    class RegionPlane2(region: Region, plane: Int) {
//        companion object {
//            const val CHUNK_SIZE = Region.SIZE shr 3
//        }
//    }

    class RegionFlags {

        //        val members: Boolean = false
//        val baseX: Int = 0
//        val baseY: Int = 0
//        private val clippingFlags: Array<IntArray>
//        private val landscape: Array<BooleanArray>
//        private val projectile = false
        constructor(plane: Int, x: Int, y: Int)
    }


    private val regions = arrayOfNulls<Region>(SIZE * SIZE)

    init {
        println("Regions: ${regions.size}")
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

    fun getRegion(x: Int, y: Int): Region? = regions[(x shr 6) + (y shr 6) * SIZE]
    fun getRegionPlane(position: Position) {
        val regionId = ((position.x shr 6) shl 8) or (position.y shr 6)
        println(regionId)

        val x = regionId shr 8 and 0xFF
        val y = regionId and 0xFF
        println("X: $x Y: $y")
    }

    /**
     * Initializes the region at the specified coordinates.
     */
    fun initializeRegion(position: Position) {
//        val regionId = ((position.x shr 6) shl 8) or (position.y shr 6)
//        val x = regionId shr 8 and 0xFF
//        val y = regionId and 0xFF

        /* Calculate the coordinates */
        val regionX = position.x shr 6
        val regionY = position.y shr 6

//        println("Region id=$regionId x=$x y=$y | regionX=$regionX regionY=$regionY")
        regions[regionX + regionY * SIZE] = Region(regionX, regionY)
    }

    /**
     * Gets if the set contains a region for the specified coordinates.
     */
    fun isRegionInitialized(position: Position): Boolean {
        /* Calculate the coordinates */
        val regionX = position.x shr 6
        val regionY = position.y shr 6

        /* Get if the region is not null */
        val r = regions[regionX + regionY * SIZE] != null
        return r
    }

//    fun isTeleportPermitted(z: Int, x: Int, y: Int): Boolean {
//        if (!isLandscape(z, x, y)) {
//            return false
//        }
//        val flag: Int = getClippingFlag(z, x, y)
//        return (flag and 0x12c0102) == 0 || ((flag and 0x12c0108) == 0) || ((flag and 0x12c0120) == 0) || ((flag and 0x12c0180) == 0)
//    }

//    fun isLandscape(z: Int, x: Int, y: Int): Boolean {
//        var x = x
//        var y = y
//        val region: Region = RegionManager.forId((x shr 6) shl 8 or (y shr 6))
//        Region.load(region)
//        if (!region.isHasFlags() || region.getPlanes().get(z).getFlags().getLandscape() == null) {
//            return false
//        }
//        x -= (x shr 6) shl 6
//        y -= (y shr 6) shl 6
//        return region.getPlanes().get(z).getFlags().getLandscape().get(x).get(y)
//    }

//    fun getClippingFlag(z: Int, x: Int, y: Int): Int {
//        var x = x
//        var y = y
//        val region: Region = RegionManager.forId((x shr 6) shl 8 or (y shr 6))
//        Region.load(region)
//        if (!region.isHasFlags()) {
//            return -1
//        }
//        x -= (x shr 6) shl 6
//        y -= (y shr 6) shl 6
//        return region.getPlanes().get(z).getFlags().getClippingFlags().get(x).get(y)
//    }
}


