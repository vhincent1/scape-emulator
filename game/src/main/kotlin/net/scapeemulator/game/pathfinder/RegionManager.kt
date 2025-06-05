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

import net.scapeemulator.game.model.Position

class RegionManager {

    companion object {
        /**
         * The size of one side of the region array.
         */
        const val SIZE: Int = 256
    }

    val REGION_CACHE: Map<Int, Region> = HashMap<Int, Region>()

    class Region(val x: Int, val y: Int) {
        companion object {
            const val SIZE = 64
        }
//        val planes =
    }

    private val regionPlanes = arrayOfNulls<RegionPlane>(SIZE * SIZE)

    init {
        println("Regions: ${regionPlanes.size}")
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

    fun getRegion(x: Int, y: Int): RegionPlane? =
        regionPlanes[(x shr 6) + (y shr 6) * SIZE]

    fun getRegionPlane(position: Position) {
        val regionId = ((position.x shr 6) shl 8) or (position.y shr 6)
    }

    /**
     * Initializes the region at the specified coordinates.
     */
    fun initializeRegion(position: Position) {
        /* Calculate the coordinates */
        val regionX = position.x shr 6
        val regionY = position.y shr 6
        regionPlanes[regionX + regionY * SIZE] = RegionPlane()
    }

    /**
     * Gets if the set contains a region for the specified coordinates.
     */
    fun isRegionInitialized(position: Position): Boolean {
        /* Calculate the coordinates */
        val regionX = position.x shr 6
        val regionY = position.y shr 6

        /* Get if the region is not null */
        val r = regionPlanes[regionX + regionY * SIZE] != null
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
