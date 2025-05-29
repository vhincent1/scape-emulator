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
package net.scapeemulator.game.model

class RegionManager {
    /**
     * The regions for the traversal data.
     */
    private val regions = arrayOfNulls<Region>(SIZE * SIZE)

    init {
        println("Regions: ${regions.size}")
    }

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
//        println("Init")
        /* Calculate the coordinates */
        val regionX = position.x shr 6
        val regionY = position.y shr 6
        regions[regionX + regionY * SIZE] = Region()
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
//        println("R $r")
        return r//todo fix
//        return true
    }

    companion object {
        /**
         * The size of one side of the region array.
         */
        const val SIZE: Int = 256
    }
}
