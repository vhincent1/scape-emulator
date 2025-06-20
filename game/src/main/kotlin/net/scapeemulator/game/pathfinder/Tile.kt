/**
 * scape-emulator-final
 * Copyright (c) 2014 Justin Conner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation either version 3 of the License or
 * (at your option) any later version.
 *
 * This program is distributed in  the hope that it will be useful
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not see <http:></http:>//www.gnu.org/license/>.
 */
package net.scapeemulator.game.pathfinder

class Tile @JvmOverloads constructor(var flags: Int = NONE) {

    fun set(flag: Int) {
        flags = flags or flag
    }

    fun unset(flag: Int) {
        flags = flags and 0xffff - flag
    }

    fun isActive(flag: Int): Boolean = (flags and flag) != 0
    fun isInactive(flag: Int): Boolean = (flags and flag) == 0
    fun flags(): Int = flags

    fun canFollow() = (flags and FLAG_FOLLOWERS) != 0
    fun isMembers() = (flags and FLAG_MEMBERS) != 0
    fun canRandomEvent() = (flags and FLAG_RANDOM_EVENT) != 0
    fun canFire() = (flags and FLAG_FIRES) != 0
    fun canCannon() = (flags and FLAG_CANNON) != 0

    override fun toString(): String = "Tile(flags=$flags follow=${canFollow()} members=${isMembers()} random=${canRandomEvent()} " +
            "fire=${canFire()} cannon=${canCannon()})"


    companion object {
        const val FLAG_CLIP: Int = 0x1
        const val FLAG_BRIDGE: Int = 0x2
        const val /* no followers */FLAG_FOLLOWERS = 0 shl 1
        const val /* no random events */FLAG_RANDOM_EVENT = 1 shl 2
        const val /* no fires */FLAG_FIRES = 1 shl 2
        const val /* members only */FLAG_MEMBERS = 1 shl 3
        const val /* no cannons */FLAG_CANNON = 1 shl 4
        const val /* Each of the flags for walls */WALL_NORTH: Int = 0x1
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

        /* Traversal flags */
        const val PREVENT_NORTH = 0x12c0120
        const val PREVENT_EAST = 0x12c0180
        const val PREVENT_NORTHEAST = 0x12c01e0
        const val PREVENT_SOUTH = 0x12c0102
        const val PREVENT_SOUTHEAST = 0x12c0183
        const val PREVENT_WEST = 0x12c0108
        const val PREVENT_SOUTHWEST = 0x12c010e
        const val PREVENT_NORTHWEST = 0x12c0138
    }
}
