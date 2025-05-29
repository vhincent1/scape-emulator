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

/**
 * Created by Hadyn Richard
 */
class Tile
/**
 * Constructs a new [Tile];
 */ @JvmOverloads constructor(
    /**
     * The flags for the tile.
     */
    var flags: Int = NONE
) {
    /**
     * Constructs a new [Tile];
     */

    /**
     * Sets a flag for the tile.
     */
    fun set(flag: Int) {
        flags = flags or flag
    }

    /**
     * Unsets a flag for the tile.
     */
    fun unset(flag: Int) {
        flags = flags and flag.inv()
    }

    /**
     * Gets if a flag is active.
     * @param flag The flag to check for if it is active.
     * @return If the flag is active.
     */
    fun isActive(flag: Int): Boolean {
        return (flags and flag) != 0
    }

    /**
     * Gets if a flag is inactive.
     * @param flag The flag to check for if it is inactive.
     * @return If the flag is inactive.
     */
    fun isInactive(flag: Int): Boolean {
        return (flags and flag) == 0
    }

    /**
     * Gets the flags for the tile.
     */
    fun flags(): Int {
        return flags
    }

    companion object {
        /**
         * The flags for each of the traversals.
         */
        const val  /* Each of the flags for walls */WALL_NORTH: Int = 0x1
        const val WALL_SOUTH: Int = 0x2
        const val WALL_EAST: Int = 0x4
        const val WALL_WEST: Int = 0x8
        const val WALL_NORTH_EAST: Int = 0x10
        const val WALL_NORTH_WEST: Int = 0x20
        const val WALL_SOUTH_EAST: Int = 0x40
        const val WALL_SOUTH_WEST: Int = 0x80

        /* Each of the occupant flags for both impenetrable and normal */
        const val OCCUPANT: Int = 0x8000
        const val IMPENETRABLE_OCCUPANT: Int = 0x10000

        /* Each of the impenetrable wall flags, meaning projectiles cannot fly over these */
        const val IMPENETRABLE_WALL_NORTH: Int = 0x100
        const val IMPENETRABLE_WALL_SOUTH: Int = 0x200
        const val IMPENETRABLE_WALL_EAST: Int = 0x400
        const val IMPENETRABLE_WALL_WEST: Int = 0x800
        const val IMPENETRABLE_WALL_NORTH_EAST: Int = 0x800
        const val IMPENETRABLE_WALL_NORTH_WEST: Int = 0x1000
        const val IMPENETRABLE_WALL_SOUTH_EAST: Int = 0x2000
        const val IMPENETRABLE_WALL_SOUTH_WEST: Int = 0x4000

        /* The other flags */
        const val BLOCKED: Int = 0x20000
        const val BRIDGE: Int = 0x40000
        const val NONE: Int = 0x0
    }
}
