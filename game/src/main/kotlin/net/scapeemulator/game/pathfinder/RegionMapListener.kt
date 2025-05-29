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
import net.scapeemulator.game.model.GameObject
import net.scapeemulator.game.model.ObjectType
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.model.RegionManager

class RegionMapListener(private val manager: RegionManager) : MapListener {
    override fun tileDecoded(flags: Int, position: Position) {
        if (!manager.isRegionInitialized(position))
            manager.initializeRegion(position)
//            println("Position")

    }

    override fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position) {
        if (!manager.isRegionInitialized(position)) manager.initializeRegion(position)

        val region = manager.getRegion(position)
        region!!.addObject(GameObject(id, type.id, position, rotation))
    }
}
