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

import net.scapeemulator.game.model.*
import java.util.*


/**
 * Created by Hadyn Richard
 */
class TraversalMap
/**
 * Constructs a new [TraversalMap];
 */
{
    /**
     * The regions for the traversal data.
     */
    private val regions = arrayOfNulls<Region>(SIZE * SIZE)

    /**
     * Created by Hadyn Richard
     */
    private inner class Region {
        /**
         * The flags within the region.
         */
        private val tiles: Array<Array<Tile?>> =
            Array(MAXIMUM_PLANE) {
                arrayOfNulls(REGION_SIZE * REGION_SIZE)
            }

        /**
         * Constructs a new [Region];
         */
        init {
            for (i in 0 until MAXIMUM_PLANE) {
                for (j in 0 until REGION_SIZE * REGION_SIZE) {
                    tiles[i][j] = Tile()
                }
            }
        }

        fun getTile(plane: Int, x: Int, y: Int): Tile? {
            return tiles[plane][x + y * REGION_SIZE]
        }
    }

    /**
     * Initializes the region at the specified coordinates.
     */
    fun initializeRegion(x: Int, y: Int) {
        /* Calculate the coordinates */

        val regionX = x shr 6
        val regionY = y shr 6

        regions[regionX + regionY * SIZE] = Region()
    }

    /**
     * Gets if the set contains a region for the specified coordinates.
     */
    fun regionInitialized(x: Int, y: Int): Boolean {
        /* Calculate the coordinates */

        val regionX = x shr 6
        val regionY = y shr 6

        /* Get if the region is not null */
        return regions[regionX + regionY * SIZE] != null
    }

    fun markWall(
        rotation: Int,
        plane: Int,
        x: Int,
        y: Int,
        type: ObjectType?,
        impenetrable: Boolean
    ) {
        when (type) {
            ObjectType.STRAIGHT_WALL -> {
                if (rotation == ObjectOrientation.WEST) {
                    set(plane, x, y, Tile.WALL_WEST)
                    set(plane, x - 1, y, Tile.WALL_EAST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_WEST)
                        set(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                    }
                }
                if (rotation == ObjectOrientation.NORTH) {
                    set(plane, x, y, Tile.WALL_NORTH)
                    set(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH)
                        set(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }
                if (rotation == ObjectOrientation.EAST) {
                    set(plane, x, y, Tile.WALL_EAST)
                    set(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_EAST)
                        set(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }
                if (rotation == ObjectOrientation.SOUTH) {
                    set(plane, x, y, Tile.WALL_SOUTH)
                    set(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH)
                        set(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_2 -> {
                if (rotation == ObjectOrientation.WEST) {
                    set(
                        plane,
                        x,
                        y,
                        Tile.WALL_WEST or Tile.WALL_NORTH
                    )
                    set(plane, x - 1, y, Tile.WALL_EAST)
                    set(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        set(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_NORTH
                        )
                        set(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                        set(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }
                if (rotation == ObjectOrientation.NORTH) {
                    set(
                        plane,
                        x,
                        y,
                        Tile.WALL_EAST or Tile.WALL_NORTH
                    )
                    set(plane, x, y + 1, Tile.WALL_SOUTH)
                    set(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        set(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_NORTH
                        )
                        set(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                        set(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }
                if (rotation == ObjectOrientation.EAST) {
                    set(
                        plane,
                        x,
                        y,
                        Tile.WALL_EAST or Tile.WALL_SOUTH
                    )
                    set(plane, x + 1, y, Tile.WALL_WEST)
                    set(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        set(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH
                        )
                        set(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                        set(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
                if (rotation == ObjectOrientation.SOUTH) {
                    set(
                        plane,
                        x,
                        y,
                        Tile.WALL_WEST or Tile.WALL_SOUTH
                    )
                    set(plane, x - 1, y, Tile.WALL_EAST)
                    set(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        set(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_SOUTH
                        )
                        set(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                        set(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_1, ObjectType.TYPE_3 -> {
                if (rotation == ObjectOrientation.WEST) {
                    set(plane, x, y, Tile.WALL_NORTH_WEST)
                    set(plane, x - 1, y + 1, Tile.WALL_SOUTH_EAST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_WEST)
                        set(
                            plane,
                            x - 1,
                            y + 1,
                            Tile.IMPENETRABLE_WALL_SOUTH_EAST
                        )
                    }
                }
                if (rotation == ObjectOrientation.NORTH) {
                    set(plane, x, y, Tile.WALL_NORTH_EAST)
                    set(plane, x + 1, y + 1, Tile.WALL_SOUTH_WEST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_EAST)
                        set(
                            plane,
                            x + 1,
                            y + 1,
                            Tile.IMPENETRABLE_WALL_SOUTH_WEST
                        )
                    }
                }
                if (rotation == ObjectOrientation.EAST) {
                    set(plane, x, y, Tile.WALL_SOUTH_EAST)
                    set(plane, x + 1, y - 1, Tile.WALL_NORTH_WEST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_EAST)
                        set(
                            plane,
                            x + 1,
                            y - 1,
                            Tile.IMPENETRABLE_WALL_NORTH_WEST
                        )
                    }
                }
                if (rotation == ObjectOrientation.SOUTH) {
                    set(plane, x, y, Tile.WALL_SOUTH_WEST)
                    set(plane, x - 1, y - 1, Tile.WALL_NORTH_EAST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_WEST)
                        set(
                            plane,
                            x - 1,
                            y - 1,
                            Tile.IMPENETRABLE_WALL_NORTH_EAST
                        )
                    }
                }
            }

            else -> {}
        }
    }

    fun unmarkWall(
        rotation: Int,
        plane: Int,
        x: Int,
        y: Int,
        type: ObjectType?,
        impenetrable: Boolean
    ) {
        when (type) {
            ObjectType.STRAIGHT_WALL -> {
                if (rotation == ObjectOrientation.WEST) {
                    unset(plane, x, y, Tile.WALL_WEST)
                    unset(plane, x - 1, y, Tile.WALL_EAST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_WEST)
                        unset(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                    }
                }
                if (rotation == ObjectOrientation.NORTH) {
                    unset(plane, x, y, Tile.WALL_NORTH)
                    unset(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH)
                        unset(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }
                if (rotation == ObjectOrientation.EAST) {
                    unset(plane, x, y, Tile.WALL_EAST)
                    unset(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_EAST)
                        unset(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }
                if (rotation == ObjectOrientation.SOUTH) {
                    unset(plane, x, y, Tile.WALL_SOUTH)
                    unset(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH)
                        unset(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_2 -> {
                if (rotation == ObjectOrientation.WEST) {
                    unset(
                        plane,
                        x,
                        y,
                        Tile.WALL_WEST or Tile.WALL_NORTH
                    )
                    unset(plane, x - 1, y, Tile.WALL_EAST)
                    unset(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        unset(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_NORTH
                        )
                        unset(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                        unset(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }
                if (rotation == ObjectOrientation.NORTH) {
                    unset(
                        plane,
                        x,
                        y,
                        Tile.WALL_EAST or Tile.WALL_NORTH
                    )
                    unset(plane, x, y + 1, Tile.WALL_SOUTH)
                    unset(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        unset(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_NORTH
                        )
                        unset(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                        unset(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }
                if (rotation == ObjectOrientation.EAST) {
                    unset(
                        plane,
                        x,
                        y,
                        Tile.WALL_EAST or Tile.WALL_SOUTH
                    )
                    unset(plane, x + 1, y, Tile.WALL_WEST)
                    unset(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        unset(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH
                        )
                        unset(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                        unset(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
                if (rotation == ObjectOrientation.SOUTH) {
                    unset(
                        plane,
                        x,
                        y,
                        Tile.WALL_EAST or Tile.WALL_SOUTH
                    )
                    unset(plane, x, y - 1, Tile.WALL_WEST)
                    unset(plane, x - 1, y, Tile.WALL_NORTH)
                    if (impenetrable) {
                        unset(
                            plane,
                            x,
                            y,
                            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH
                        )
                        unset(plane, x, y - 1, Tile.IMPENETRABLE_WALL_WEST)
                        unset(plane, x - 1, y, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_1, ObjectType.TYPE_3 -> {
                if (rotation == ObjectOrientation.WEST) {
                    unset(plane, x, y, Tile.WALL_NORTH_WEST)
                    unset(plane, x - 1, y + 1, Tile.WALL_SOUTH_EAST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_WEST)
                        unset(
                            plane,
                            x - 1,
                            y + 1,
                            Tile.IMPENETRABLE_WALL_SOUTH_EAST
                        )
                    }
                }
                if (rotation == ObjectOrientation.NORTH) {
                    unset(plane, x, y, Tile.WALL_NORTH_EAST)
                    unset(plane, x + 1, y + 1, Tile.WALL_SOUTH_WEST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_EAST)
                        unset(
                            plane,
                            x + 1,
                            y + 1,
                            Tile.IMPENETRABLE_WALL_SOUTH_WEST
                        )
                    }
                }
                if (rotation == ObjectOrientation.EAST) {
                    unset(plane, x, y, Tile.WALL_SOUTH_EAST)
                    unset(plane, x + 1, y - 1, Tile.WALL_NORTH_WEST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_EAST)
                        unset(
                            plane,
                            x + 1,
                            y - 1,
                            Tile.IMPENETRABLE_WALL_NORTH_WEST
                        )
                    }
                }
                if (rotation == ObjectOrientation.SOUTH) {
                    unset(plane, x, y, Tile.WALL_SOUTH_WEST)
                    unset(plane, x - 1, y - 1, Tile.WALL_NORTH_EAST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_WEST)
                        unset(
                            plane,
                            x - 1,
                            y - 1,
                            Tile.IMPENETRABLE_WALL_NORTH_EAST
                        )
                    }
                }
            }

            else -> {}
        }
    }

    fun markBlocked(plane: Int, x: Int, y: Int) {
        /* Calculate the coordinates */

        val regionX = x shr 6
        val regionY = y shr 6

        /* Calculate the local coordinates */
        val localX = x and 0x3f
        val localY = y and 0x3f

        val region = regions[regionX + regionY * SIZE] ?: return

        var modifiedPlane = plane
        if ((region.getTile(1, localX, localY)?.flags()!! and Tile.BRIDGE) != 0) {
            modifiedPlane = plane - 1
        }

        region.getTile(modifiedPlane, x and 0x3f, y and 0x3f)?.set(Tile.BLOCKED)
    }

    fun markOccupant(plane: Int, x: Int, y: Int, width: Int, length: Int, impenetrable: Boolean) {
        for (offsetX in 0 until width) {
            for (offsetY in 0 until length) {
                set(plane, x + offsetX, y + offsetY, Tile.OCCUPANT)
                if (impenetrable) {
                    set(
                        plane,
                        x + offsetX,
                        y + offsetY,
                        Tile.IMPENETRABLE_OCCUPANT
                    )
                }
            }
        }
    }

    fun unmarkOccupant(plane: Int, x: Int, y: Int, width: Int, length: Int, impenetrable: Boolean) {
        for (offsetX in 0 until width) {
            for (offsetY in 0 until length) {
                unset(plane, x + offsetX, y + offsetY, Tile.OCCUPANT)
                if (impenetrable) {
                    unset(plane,
                        x + offsetX,
                        y + offsetY,
                        Tile.IMPENETRABLE_OCCUPANT
                    )
                }
            }
        }
    }

    fun markBridge(plane: Int, x: Int, y: Int) {
        set(plane, x, y, Tile.BRIDGE)
    }

    fun isTraversableNorth(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableNorth(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableNorth(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableNorth(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed north.
     */
    fun isTraversableNorth(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return isInactive(
                plane,
                x,
                y + 1,
                Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_SOUTH
            )
        }
        return isInactive(
            plane,
            x,
            y + 1,
            Tile.WALL_SOUTH or Tile.OCCUPANT or Tile.BLOCKED
        )
    }

    fun isTraversableSouth(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableSouth(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableSouth(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableSouth(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed south.
     */
    fun isTraversableSouth(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return isInactive(
                plane,
                x,
                y - 1,
                Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_NORTH
            )
        }
        return isInactive(
            plane,
            x,
            y - 1,
            Tile.WALL_NORTH or Tile.OCCUPANT or Tile.BLOCKED
        )
    }

    fun isTraversableEast(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableEast(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableEast(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableEast(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed south.
     */
    fun isTraversableEast(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return isInactive(
                plane,
                x + 1,
                y,
                Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_WEST
            )
        }
        return isInactive(
            plane,
            x + 1,
            y,
            Tile.WALL_WEST or Tile.OCCUPANT or Tile.BLOCKED
        )
    }

    fun isTraversableWest(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableWest(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableWest(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableWest(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed south.
     */
    fun isTraversableWest(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return isInactive(
                plane, x - 1, y, Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_EAST
            )
        }
        return isInactive(plane, x - 1, y, Tile.WALL_EAST or Tile.OCCUPANT or Tile.BLOCKED)
    }

    fun isTraversableNorthEast(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableNorthEast(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableNorthEast(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableNorthEast(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed south.
     */
    fun isTraversableNorthEast(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return (isInactive(
                plane,
                x + 1,
                y + 1,
                Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_WALL_SOUTH_WEST or Tile.OCCUPANT
            ) && isInactive(
                plane,
                x + 1,
                y,
                Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_OCCUPANT
            ) && isInactive(
                plane,
                x,
                y + 1,
                Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_OCCUPANT
            ))
        }
        return (isInactive(
            plane,
            x + 1,
            y + 1,
            Tile.WALL_WEST or Tile.WALL_SOUTH or Tile.WALL_SOUTH_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x + 1,
            y,
            Tile.WALL_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x,
            y + 1,
            Tile.WALL_SOUTH or Tile.OCCUPANT or Tile.BLOCKED
        ))
    }

    fun isTraversableNorthWest(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableNorthWest(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableNorthWest(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableNorthWest(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed south.
     */
    fun isTraversableNorthWest(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return (isInactive(
                plane,
                x - 1,
                y + 1,
                Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_WALL_SOUTH_EAST or Tile.OCCUPANT
            ) && isInactive(
                plane,
                x - 1,
                y,
                Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_OCCUPANT
            ) && isInactive(
                plane,
                x,
                y + 1,
                Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_OCCUPANT
            ))
        }
        return (isInactive(
            plane,
            x - 1,
            y + 1,
            Tile.WALL_EAST or Tile.WALL_SOUTH or Tile.WALL_SOUTH_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x - 1,
            y,
            Tile.WALL_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x,
            y + 1,
            Tile.WALL_SOUTH or Tile.OCCUPANT or Tile.BLOCKED
        ))
    }

    fun isTraversableSouthEast(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableSouthEast(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableSouthEast(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableSouthEast(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed south.
     */
    fun isTraversableSouthEast(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return (isInactive(
                plane,
                x + 1,
                y - 1,
                Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_WALL_NORTH_WEST or Tile.OCCUPANT
            ) && isInactive(
                plane,
                x + 1,
                y,
                Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_OCCUPANT
            ) && isInactive(
                plane,
                x,
                y - 1,
                Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_OCCUPANT
            ))
        }
        return (isInactive(
            plane,
            x + 1,
            y - 1,
            Tile.WALL_WEST or Tile.WALL_NORTH or Tile.WALL_NORTH_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x + 1,
            y,
            Tile.WALL_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x,
            y - 1,
            Tile.WALL_NORTH or Tile.OCCUPANT or Tile.BLOCKED
        ))
    }

    fun isTraversableSouthWest(plane: Int, x: Int, y: Int, size: Int): Boolean {
        for (offsetX in 0 until size) {
            for (offsetY in 0 until size) {
                if (!isTraversableSouthWest(plane, x + offsetX, y + offsetY)) {
                    return false
                }
            }
        }
        return true
    }

    fun isTraversableSouthWest(plane: Int, x: Int, y: Int): Boolean {
        return isTraversableSouthWest(plane, x, y, false)
    }

    /**
     * Tests if from the specified position it can be traversed south.
     */
    fun isTraversableSouthWest(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) {
            return (isInactive(
                plane,
                x - 1,
                y - 1,
                Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_WALL_NORTH_EAST or Tile.OCCUPANT
            )
                    && isInactive(
                plane,
                x - 1,
                y,
                Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_OCCUPANT
            ) && isInactive(
                plane,
                x,
                y - 1,
                Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_OCCUPANT
            ))
        }
        return (isInactive(
            plane,
            x - 1,
            y - 1,
            Tile.WALL_EAST or Tile.WALL_NORTH or Tile.WALL_NORTH_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x - 1,
            y,
            Tile.WALL_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane,
            x,
            y - 1,
            Tile.WALL_NORTH or Tile.OCCUPANT or Tile.BLOCKED
        ))
    }

    fun set(plane: Int, x: Int, y: Int, flag: Int) {
        /* Calculate the coordinates */

        val regionX = x shr 6
        val regionY = y shr 6

        val region = regions[regionX + regionY * SIZE] ?: return

        region.getTile(plane, x and 0x3f, y and 0x3f)?.set(flag)
    }

    fun attackPathClear(source: Mob, dest: Position, projectile: Boolean): Boolean {
        if (projectile) {
            val path: Path =
                ProjectilePathFinder.find(source.position, dest)
            if (path.isEmpty) {
                return true
            }
            var prev: Position = source.position
            while (!path.isEmpty) {
                val next: Position = path.poll()
                if (!Direction.projectileClipping(prev, next)) {
                    return false
                }
                prev = next
            }
            return true
        } else {
            return Direction.isTraversable(
                source.position,
                Direction.between(source.position, dest),
                source.size
            )
        }
    }

    fun isInactive(plane: Int, x: Int, y: Int, flag: Int): Boolean {
        /* Calculate the region coordinates */
        val regionX = x shr 6
        val regionY = y shr 6

        /* Calculate the local region coordinates */
        val localX = x and 0x3f
        val localY = y and 0x3f

        val region = regions[regionX + regionY * SIZE] ?: return false

        var modifiedPlane = plane
        if (region.getTile(1, localX, localY)?.isActive(Tile.BRIDGE)!!) {
            modifiedPlane = plane + 1
        }

        return region.getTile(modifiedPlane, localX, localY)?.isInactive(flag)!!
    }

    fun unset(plane: Int, x: Int, y: Int, flag: Int) {
        /* Calculate the coordinates */

        val regionX = x shr 6
        val regionY = y shr 6

        val region = regions[regionX + regionY * SIZE] ?: return

        region.getTile(plane, x and 0x3f, y and 0x3f)?.unset(flag)
    }

    companion object {
        /**
         * The size of one side of the region array.
         */
        const val SIZE: Int = 256

        /**
         * The size of a region.
         */
        const val REGION_SIZE: Int = 64

        /**
         * The maximum plane.
         */
        const val MAXIMUM_PLANE: Int = 4
    }
}
