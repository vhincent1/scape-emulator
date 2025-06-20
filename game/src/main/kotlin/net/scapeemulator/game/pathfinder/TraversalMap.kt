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

/**
 * Created by Hadyn Richard
 */
class TraversalMap(private val region: RegionManager) {

    fun markWall(rotation: Int, plane: Int, x: Int, y: Int, type: ObjectType?, impenetrable: Boolean) {
        when (type) {
            ObjectType.STRAIGHT_WALL -> when (rotation) {
                ObjectOrientation.WEST -> {
                    set(plane, x, y, Tile.WALL_WEST)
                    set(plane, x - 1, y, Tile.WALL_EAST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_WEST)
                        set(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                    }
                }

                ObjectOrientation.NORTH -> {
                    set(plane, x, y, Tile.WALL_NORTH)
                    set(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH)
                        set(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }

                ObjectOrientation.EAST -> {
                    set(plane, x, y, Tile.WALL_EAST)
                    set(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_EAST)
                        set(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }

                ObjectOrientation.SOUTH -> {
                    set(plane, x, y, Tile.WALL_SOUTH)
                    set(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH)
                        set(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_2 -> when (rotation) {
                ObjectOrientation.WEST -> {
                    set(plane, x, y, Tile.WALL_WEST or Tile.WALL_NORTH)
                    set(plane, x - 1, y, Tile.WALL_EAST)
                    set(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_NORTH)
                        set(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                        set(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }

                ObjectOrientation.NORTH -> {
                    set(plane, x, y, Tile.WALL_EAST or Tile.WALL_NORTH)
                    set(plane, x, y + 1, Tile.WALL_SOUTH)
                    set(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_NORTH)
                        set(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                        set(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }

                ObjectOrientation.EAST -> {
                    set(plane, x, y, Tile.WALL_EAST or Tile.WALL_SOUTH)
                    set(plane, x + 1, y, Tile.WALL_WEST)
                    set(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH)
                        set(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                        set(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }

                ObjectOrientation.SOUTH -> {
                    set(plane, x, y, Tile.WALL_WEST or Tile.WALL_SOUTH)
                    set(plane, x - 1, y, Tile.WALL_EAST)
                    set(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_SOUTH)
                        set(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                        set(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_1, ObjectType.TYPE_3 -> when (rotation) {
                ObjectOrientation.WEST -> {
                    set(plane, x, y, Tile.WALL_NORTH_WEST)
                    set(plane, x - 1, y + 1, Tile.WALL_SOUTH_EAST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_WEST)
                        set(plane, x - 1, y + 1, Tile.IMPENETRABLE_WALL_SOUTH_EAST)
                    }
                }

                ObjectOrientation.NORTH -> {
                    set(plane, x, y, Tile.WALL_NORTH_EAST)
                    set(plane, x + 1, y + 1, Tile.WALL_SOUTH_WEST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_EAST)
                        set(plane, x + 1, y + 1, Tile.IMPENETRABLE_WALL_SOUTH_WEST)
                    }
                }

                ObjectOrientation.EAST -> {
                    set(plane, x, y, Tile.WALL_SOUTH_EAST)
                    set(plane, x + 1, y - 1, Tile.WALL_NORTH_WEST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_EAST)
                        set(plane, x + 1, y - 1, Tile.IMPENETRABLE_WALL_NORTH_WEST)
                    }
                }

                ObjectOrientation.SOUTH -> {
                    set(plane, x, y, Tile.WALL_SOUTH_WEST)
                    set(plane, x - 1, y - 1, Tile.WALL_NORTH_EAST)
                    if (impenetrable) {
                        set(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_WEST)
                        set(plane, x - 1, y - 1, Tile.IMPENETRABLE_WALL_NORTH_EAST)
                    }
                }
            }

            else -> {}
        }
    }

    fun unmarkWall(rotation: Int, plane: Int, x: Int, y: Int, type: ObjectType?, impenetrable: Boolean) {
        when (type) {
            ObjectType.STRAIGHT_WALL -> when (rotation) {
                ObjectOrientation.WEST -> {
                    unset(plane, x, y, Tile.WALL_WEST)
                    unset(plane, x - 1, y, Tile.WALL_EAST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_WEST)
                        unset(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                    }
                }

                ObjectOrientation.NORTH -> {
                    unset(plane, x, y, Tile.WALL_NORTH)
                    unset(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH)
                        unset(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }

                ObjectOrientation.EAST -> {
                    unset(plane, x, y, Tile.WALL_EAST)
                    unset(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_EAST)
                        unset(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }

                ObjectOrientation.SOUTH -> {
                    unset(plane, x, y, Tile.WALL_SOUTH)
                    unset(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH)
                        unset(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_2 -> when (rotation) {
                ObjectOrientation.WEST -> {
                    unset(plane, x, y, Tile.WALL_WEST or Tile.WALL_NORTH)
                    unset(plane, x - 1, y, Tile.WALL_EAST)
                    unset(plane, x, y + 1, Tile.WALL_SOUTH)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_NORTH)
                        unset(plane, x - 1, y, Tile.IMPENETRABLE_WALL_EAST)
                        unset(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                    }
                }

                ObjectOrientation.NORTH -> {
                    unset(plane, x, y, Tile.WALL_EAST or Tile.WALL_NORTH)
                    unset(plane, x, y + 1, Tile.WALL_SOUTH)
                    unset(plane, x + 1, y, Tile.WALL_WEST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_NORTH)
                        unset(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH)
                        unset(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                    }
                }

                ObjectOrientation.EAST -> {
                    unset(plane, x, y, Tile.WALL_EAST or Tile.WALL_SOUTH)
                    unset(plane, x + 1, y, Tile.WALL_WEST)
                    unset(plane, x, y - 1, Tile.WALL_NORTH)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH)
                        unset(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST)
                        unset(plane, x, y - 1, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }

                ObjectOrientation.SOUTH -> {
                    unset(plane, x, y, Tile.WALL_EAST or Tile.WALL_SOUTH)
                    unset(plane, x, y - 1, Tile.WALL_WEST)
                    unset(plane, x - 1, y, Tile.WALL_NORTH)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH)
                        unset(plane, x, y - 1, Tile.IMPENETRABLE_WALL_WEST)
                        unset(plane, x - 1, y, Tile.IMPENETRABLE_WALL_NORTH)
                    }
                }
            }

            ObjectType.TYPE_1, ObjectType.TYPE_3 -> when (rotation) {
                ObjectOrientation.WEST -> {
                    unset(plane, x, y, Tile.WALL_NORTH_WEST)
                    unset(plane, x - 1, y + 1, Tile.WALL_SOUTH_EAST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_WEST)
                        unset(plane, x - 1, y + 1, Tile.IMPENETRABLE_WALL_SOUTH_EAST)
                    }
                }

                ObjectOrientation.NORTH -> {
                    unset(plane, x, y, Tile.WALL_NORTH_EAST)
                    unset(plane, x + 1, y + 1, Tile.WALL_SOUTH_WEST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_NORTH_EAST)
                        unset(plane, x + 1, y + 1, Tile.IMPENETRABLE_WALL_SOUTH_WEST)
                    }
                }

                ObjectOrientation.EAST -> {
                    unset(plane, x, y, Tile.WALL_SOUTH_EAST)
                    unset(plane, x + 1, y - 1, Tile.WALL_NORTH_WEST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_EAST)
                        unset(plane, x + 1, y - 1, Tile.IMPENETRABLE_WALL_NORTH_WEST)
                    }
                }

                ObjectOrientation.SOUTH -> {
                    unset(plane, x, y, Tile.WALL_SOUTH_WEST)
                    unset(plane, x - 1, y - 1, Tile.WALL_NORTH_EAST)
                    if (impenetrable) {
                        unset(plane, x, y, Tile.IMPENETRABLE_WALL_SOUTH_WEST)
                        unset(plane, x - 1, y - 1, Tile.IMPENETRABLE_WALL_NORTH_EAST)
                    }
                }
            }

            else -> {}
        }
    }

    fun markOccupant(plane: Int, x: Int, y: Int, width: Int, length: Int, impenetrable: Boolean) {
        offset(width, length) { offsetX, offsetY ->
            set(plane, x + offsetX, y + offsetY, Tile.OCCUPANT)
            if (impenetrable) set(plane, x + offsetX, y + offsetY, Tile.IMPENETRABLE_OCCUPANT)
        }
    }

    fun unmarkOccupant(plane: Int, x: Int, y: Int, width: Int, length: Int, impenetrable: Boolean) {
        offset(width, length) { offsetX, offsetY ->
            unset(plane, x + offsetX, y + offsetY, Tile.OCCUPANT)
            if (impenetrable) unset(plane, x + offsetX, y + offsetY, Tile.IMPENETRABLE_OCCUPANT)
        }
    }

    private fun offset(width: Int, length: Int? = null, block: (Int, Int) -> Unit) {
        for (offsetX in 0 until width) for (offsetY in 0 until (length ?: width)) block(offsetX, offsetY)
    }

    private fun isTraversableNorthEast(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return (isInactive(
            plane, x + 1, y + 1,
            Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_WALL_SOUTH_WEST or Tile.OCCUPANT
        ) && isInactive(plane, x + 1, y, Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_OCCUPANT)
                && isInactive(plane, x, y + 1, Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_OCCUPANT))
        return (isInactive(
            plane, x + 1, y + 1,
            Tile.WALL_WEST or Tile.WALL_SOUTH or Tile.WALL_SOUTH_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane, x + 1, y,
            Tile.WALL_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane, x, y + 1, Tile.WALL_SOUTH or Tile.OCCUPANT or Tile.BLOCKED
        ))
    }

    private fun isTraversableNorthWest(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return (isInactive(
            plane, x - 1, y + 1,
            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_WALL_SOUTH_EAST or Tile.OCCUPANT
        ) && isInactive(
            plane, x - 1, y,
            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_OCCUPANT
        ) && isInactive(
            plane, x, y + 1,
            Tile.IMPENETRABLE_WALL_SOUTH or Tile.IMPENETRABLE_OCCUPANT
        ))
        return (isInactive(
            plane, x - 1, y + 1,
            Tile.WALL_EAST or Tile.WALL_SOUTH or Tile.WALL_SOUTH_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane, x - 1, y,
            Tile.WALL_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane, x, y + 1,
            Tile.WALL_SOUTH or Tile.OCCUPANT or Tile.BLOCKED
        ))
    }

    private fun isTraversableSouthEast(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return (isInactive(
            plane, x + 1, y - 1,
            Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_WALL_NORTH_WEST or Tile.OCCUPANT
        ) && isInactive(
            plane, x + 1, y,
            Tile.IMPENETRABLE_WALL_WEST or Tile.IMPENETRABLE_OCCUPANT
        ) && isInactive(
            plane, x, y - 1,
            Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_OCCUPANT
        ))

        return (isInactive(
            plane, x + 1, y - 1,
            Tile.WALL_WEST or Tile.WALL_NORTH or Tile.WALL_NORTH_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane, x + 1, y,
            Tile.WALL_WEST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane, x, y - 1,
            Tile.WALL_NORTH or Tile.OCCUPANT or Tile.BLOCKED
        ))
    }

    private fun isTraversableSouthWest(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return (isInactive(
            plane, x - 1, y - 1,
            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_WALL_NORTH_EAST or Tile.OCCUPANT
        ) && isInactive(
            plane, x - 1, y,
            Tile.IMPENETRABLE_WALL_EAST or Tile.IMPENETRABLE_OCCUPANT
        ) && isInactive(
            plane, x, y - 1,
            Tile.IMPENETRABLE_WALL_NORTH or Tile.IMPENETRABLE_OCCUPANT
        ))
        return (isInactive(
            plane, x - 1, y - 1,
            Tile.WALL_EAST or Tile.WALL_NORTH or Tile.WALL_NORTH_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(
            plane, x - 1, y, Tile.WALL_EAST or Tile.OCCUPANT or Tile.BLOCKED
        ) && isInactive(plane, x, y - 1, Tile.WALL_NORTH or Tile.OCCUPANT or Tile.BLOCKED))
    }

    fun isTraversable(
        direction: Direction,
        position: Position,
        size: Int? = null,
        projectile: Boolean = false
    ): Boolean {
        val (x, y, plane) = position
        var isTraversable = false
        if (size != null && !projectile) {
            offset(size) { offsetX, offsetY ->
                val tX = x + offsetX
                val tY = y + offsetY
                isTraversable = when (direction) {
                    Direction.NONE -> true
                    Direction.NORTH -> isTraversableNorth(plane, tX, tY, false)
                    Direction.NORTH_EAST -> isTraversableNorthEast(plane, tX, tY, false)
                    Direction.EAST -> isTraversableEast(plane, tX, tY, false)
                    Direction.SOUTH_EAST -> isTraversableSouthEast(plane, tX, tY, false)
                    Direction.SOUTH -> isTraversableSouth(plane, tX, tY, false)
                    Direction.SOUTH_WEST -> isTraversableSouthWest(plane, tX, tY, false)
                    Direction.WEST -> isTraversableWest(plane, tX, tY, false)
                    Direction.NORTH_WEST -> isTraversableNorthWest(plane, tX, tY, false)
                }
            }
        } else return when (direction) {
            Direction.NONE -> return true
            Direction.NORTH -> isTraversableNorth(plane, x, y, true)
            Direction.NORTH_EAST -> isTraversableNorthEast(plane, x, y, true)
            Direction.EAST -> isTraversableEast(plane, x, y, true)
            Direction.SOUTH_EAST -> isTraversableSouthEast(plane, x, y, true)
            Direction.SOUTH -> isTraversableSouth(plane, x, y, true)
            Direction.SOUTH_WEST -> isTraversableSouthWest(plane, x, y, true)
            Direction.WEST -> isTraversableWest(plane, x, y, true)
            Direction.NORTH_WEST -> isTraversableNorthWest(plane, x, y, true)
        }
        return isTraversable
    }

    private fun isTraversableNorth(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return isInactive(plane, x, y + 1, Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_SOUTH)
        return isInactive(plane, x, y + 1, Tile.WALL_SOUTH or Tile.OCCUPANT or Tile.BLOCKED)
    }

    private fun isTraversableSouth(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return isInactive(plane, x, y - 1, Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_NORTH)
        return isInactive(plane, x, y - 1, Tile.WALL_NORTH or Tile.OCCUPANT or Tile.BLOCKED)
    }

    private fun isTraversableEast(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return isInactive(plane, x + 1, y, Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_WEST)
        return isInactive(plane, x + 1, y, Tile.WALL_WEST or Tile.OCCUPANT or Tile.BLOCKED)
    }

    private fun isTraversableWest(plane: Int, x: Int, y: Int, impenetrable: Boolean): Boolean {
        if (impenetrable) return isInactive(plane, x - 1, y, Tile.IMPENETRABLE_OCCUPANT or Tile.IMPENETRABLE_WALL_EAST)
        return isInactive(plane, x - 1, y, Tile.WALL_EAST or Tile.OCCUPANT or Tile.BLOCKED)
    }

    fun attackPathClear(source: Mob, dest: Position, projectile: Boolean): Boolean {
        if (projectile) {
            val path: Path = ProjectilePathFinder.find(source.position, dest)
            if (path.isEmpty) return true
            var prev: Position = source.position
            while (!path.isEmpty) {
                val next: Position = path.poll()
                if (!Direction.projectileClipping(prev, next)) return false
                prev = next
            }
            return true
        } else {
            return Direction.isTraversable(source.position, Direction.between(source.position, dest), source.size)
        }
    }

    private fun isInactive(plane: Int, x: Int, y: Int, flag: Int): Boolean {
        /* Calculate the region coordinates */
        val regionX = x shr 6
        val regionY = y shr 6
        /* Calculate the local region coordinates */
        val localX = x and 0x3f
        val localY = y and 0x3f
        val region = region.getRegion(x, y) ?: return false
        var modifiedPlane = plane
        if (region.getTile(1, localX, localY)?.isActive(Tile.BRIDGE)!!)
            modifiedPlane = plane + 1
        return region.getTile(modifiedPlane, localX, localY)?.isInactive(flag)!!
    }

    private fun set(plane: Int, x: Int, y: Int, flag: Int) {
        /* Calculate the coordinates */
        val regionX = x shr 6
        val regionY = y shr 6
        /* Calculate the local region coordinates */
        val localX = x and 0x3f
        val localY = y and 0x3f
        val region = region.getRegion(x, y) ?: return
        region.getTile(plane, localX, localY)?.set(flag)
    }

    private fun unset(plane: Int, x: Int, y: Int, flag: Int) {
        /* Calculate the coordinates */
        val regionX = x shr 6
        val regionY = y shr 6
        /* Calculate the local region coordinates */
        val localX = x and 0x3f
        val localY = y and 0x3f
        val region = region.getRegion(x, y) ?: return
        region.getTile(plane, localX, localY)?.unset(flag)
    }

    fun markBridge(plane: Int, x: Int, y: Int) = set(plane, x, y, Tile.BRIDGE)
    fun markBlocked(plane: Int, x: Int, y: Int) {
        /* Calculate the coordinates */
        val regionX = x shr 6
        val regionY = y shr 6
        /* Calculate the local coordinates */
        val localX = x and 0x3f
        val localY = y and 0x3f
        val region = region.getRegion(x, y) ?: return
        var modifiedPlane = plane
        if ((region.getTile(1, localX, localY)?.flags()!! and Tile.BRIDGE) != 0)
            modifiedPlane = plane - 1
        region.getTile(modifiedPlane, localX, localY)?.set(Tile.BLOCKED)
    }
}