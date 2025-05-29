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

import net.scapeemulator.game.cache.MapListener
import net.scapeemulator.game.cache.ObjectDefinitions
import net.scapeemulator.game.model.ObjectType
import net.scapeemulator.game.model.Position

class TraversalMapListener(private val map: TraversalMap) : MapListener {
    override fun tileDecoded(flags: Int, position: Position) {
        if ((flags and FLAG_BRIDGE) != 0) {
            map.markBridge(position)
        }

        if ((flags and FLAG_CLIP) != 0) {
            map.markBlocked(position)
        }
    }

    override fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position) {
        val def = ObjectDefinitions.forId(id) ?: return
//        println(def?.name)
        if (!def.isSolid) return
        if (type.isWall) { // type 0-3
            map.markWall(position, rotation, type, def.isImpenetrable)
        }

        if (type.id >= 9 && type.id <= 12) {
            /* Flip the length and width if the object is rotated */
            var width = def.width
            var length = def.length
            if (rotation == 1 || rotation == 3) {
                width = def.length
                length = def.width
            }
            map.markOccupant(position, width, length)
        }

    }


    companion object {
        private const val FLAG_CLIP = 0x1
        private const val FLAG_BRIDGE = 0x2
    }
}
