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

import net.scapeemulator.game.model.GameObject
import net.scapeemulator.game.model.Mob
import net.scapeemulator.game.model.Position

/**
 * Created by Hadyn Richard
 */
abstract class PathFinder {
    fun find(mob: Mob, gameObject: GameObject): Path? {
        /* Get the current position of the player */
        val position = mob.position

        /* Get the position of the object */
        val obj = gameObject.position

        val def = gameObject.definition ?: return null


        /* Get the object's width and length */
        var objWidth: Int = gameObject.definition!!.width
        var objLength: Int = gameObject.definition!!.length
        if (gameObject.rotation == 1 || gameObject.rotation == 3) {
            objWidth = gameObject.definition!!.length
            objLength = gameObject.definition!!.width
        }

        if (gameObject.type != 10 && gameObject.type != 11 && gameObject.type != 22) {
            objLength = 0
            objWidth = objLength
        }

        /* Get the scene base x and y coordinates */
        val baseLocalX: Int = position.getBaseLocalX()
        val baseLocalY: Int = position.getBaseLocalY()

        /* Calculate the local x and y coordinates */
        val destLocalX = obj.x - baseLocalX
        val destLocalY = obj.y - baseLocalY

        val path = find(
            Position(baseLocalX, baseLocalY, position.height),
            64,
            position.getLocalX(),
            position.getLocalY(),
            destLocalX,
            destLocalY,
            objWidth,
            objLength,
            mob.size
        )
        return path
    }

    fun find(mob: Mob, destX: Int, destY: Int): Path? {
        /* Get the current position of the player */

        val position = mob.position

        /* Get the scene base x and y coordinates */
        val baseLocalX: Int = position.getBaseLocalX()
        val baseLocalY: Int = position.getBaseLocalY()

        /* Calculate the local x and y coordinates */
        val destLocalX = destX - baseLocalX
        val destLocalY = destY - baseLocalY

        return find(
            Position(baseLocalX, baseLocalY, position.height),
            64,
            position.getLocalX(),
            position.getLocalY(),
            destLocalX,
            destLocalY,
            0,
            0,
            mob.size
        )
    }

    fun find(position: Position, radius: Int, srcX: Int, srcY: Int, destX: Int, destY: Int): Path? {
        return find(position, radius, srcX, srcY, destX, destY, 0, 0, 1)
    }

    abstract fun find(
        position: Position,
        radius: Int,
        srcX: Int,
        srcY: Int,
        destX: Int,
        destY: Int,
        objWidth: Int,
        objHeight: Int,
        size: Int
    ): Path?
}
