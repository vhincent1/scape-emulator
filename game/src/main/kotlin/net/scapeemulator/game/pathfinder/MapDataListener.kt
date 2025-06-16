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

import net.scapeemulator.cache.def.ObjectDefinition
import net.scapeemulator.game.cache.MapSet
import net.scapeemulator.game.cache.ObjectDefinitions
import net.scapeemulator.game.model.ObjectType
import net.scapeemulator.game.model.Position

abstract class MapListener {
    abstract fun tileDecoded(flags: Int, position: Position)

    abstract fun objectDecoded(
        id: Int,
        rotation: Int,
        type: ObjectType,
        position: Position
    )
}

abstract class MapListenerAdapter : MapListener() {
    override fun tileDecoded(flags: Int, position: Position) {}

    override fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position) {}
}

class MapDataListener(traversalMap: TraversalMap) : MapListenerAdapter() {

    private val traversalMap: TraversalMap = traversalMap

    override fun tileDecoded(flags: Int, position: Position) {
        if ((flags and MapSet.BRIDGE_FLAG) != 0) {
            traversalMap.markBridge(position.height, position.x, position.y)
        }

        if ((flags and MapSet.FLAG_CLIP) != 0) {
            traversalMap.markBlocked(position.height, position.x, position.y)
        }
    }

    override fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position) {
        val def: ObjectDefinition = ObjectDefinitions.forId(id) ?: return
        if (!def.isSolid) {
            return
        }

        if (!traversalMap.regionInitialized(position.x, position.y)) {
            traversalMap.initializeRegion(position.x, position.y)
        }

        if (type.isWall) {
            traversalMap.markWall(
                rotation,
                position.height,
                position.x,
                position.y,
                type,
                def.isImpenetrable
            )
        }

        if (type.id >= 9 && type.id <= 12) {
            /* Flip the length and width if the object is rotated */

            var width: Int = def.width
            var length: Int = def.length
            if (1 == rotation || rotation == 3) {
                width = def.length
                length = def.width
            }

            traversalMap.markOccupant(
                position.height,
                position.x,
                position.y,
                width,
                length,
                def.isImpenetrable
            )
        }
    }
}

