/**
 * scape-emulator-final
 * Copyright (c) 2014 Justin Conner
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * This program is distributed in  the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http:></http:>//www.gnu.org/license/>.
 */
package net.scapeemulator.game.model

import net.scapeemulator.game.pathfinder.Tile
import java.util.*

/**
 * Created by Hadyn Richard
 */
class Region {
    /**
     * The flags within the region.
     */
    private val tiles: Array<Array<Tile?>>

    /**
     * The objects within the region.
     */
    private val objects: MutableMap<Position, GameObject> = HashMap()

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