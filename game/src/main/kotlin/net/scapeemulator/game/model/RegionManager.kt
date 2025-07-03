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

import net.scapeemulator.game.pathfinder.Tile
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


class Region {
    private val tiles: Array<Array<Tile?>> = Array(MAXIMUM_PLANE) { arrayOfNulls(REGION_SIZE * REGION_SIZE) }
    private val objects: MutableMap<Position, GameObject> = HashMap<Position, GameObject>()
    private val mobs = CopyOnWriteArrayList<Mob>()

    fun add(mob: Mob) {
        mobs.add(mob)
    }

    /**
     * Constructs a new [net.scapeemulator.game.model.region.Region];
     */
    init {
        for (i in 0 until MAXIMUM_PLANE)
            for (j in 0 until REGION_SIZE * REGION_SIZE)
                tiles[i][j] = Tile()
    }

    fun getTile(plane: Int, x: Int, y: Int): Tile? = tiles[plane][x + y * REGION_SIZE]
    fun addObject(`object`: GameObject) {
        objects[`object`.position] = `object`
    }

    fun getObject(position: Position): GameObject? = objects[position]
    fun getObjects(): Collection<GameObject> = Collections.unmodifiableCollection(objects.values)

    companion object {
        const val REGION_SIZE: Int = 64
        const val MAXIMUM_PLANE: Int = 4
    }
}

class RegionManager {
    /**
     * The regions for the traversal data.
     */
    internal val regions: Array<Region?> = arrayOfNulls(SIZE * SIZE)

    fun getRegion(position: Position): Region? {
        val id = ((position.x shr 6) shl 8) or (position.y shr 6)
        val region = getRegion(position.x, position.y)
        if (region == null) initializeRegion(position)
        return region
    }

    //traversal map
    fun getRegion(x: Int, y: Int): Region? = regions[(x shr 6) + (y shr 6) * SIZE]

    /**
     * Initializes the region at the specified coordinates.
     */
    fun initializeRegion(position: Position) {
        val regionId = ((position.x shr 6) shl 8) or (position.y shr 6)
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

//    fun isTeleportPermitted(position: Position): Boolean {
//        val localX = position.x and 0x3f
//        val localY = position.y and 0x3f
//        val flag = getRegion(position)?.getTile(position.height, localX, localY)?.flags() ?: return false
//        return (flag and 0x12c0102) == 0 || ((flag and 0x12c0108) == 0) || ((flag and 0x12c0120) == 0) || ((flag and 0x12c0180) == 0)
//    }

    companion object {
        /**
         * The size of one side of the region array.
         */
        const val SIZE: Int = 256
    }
}
