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

import net.scapeemulator.game.model.Position
import java.util.*

/**
 * Represents a path found by a `PathFinder` between two points.
 * @author Graham Edgecombe
 */
class Path
/**
 * Creates an empty path.
 */
{
    /**
     * Gets the deque backing this path.
     * @return The deque backing this path.
     */
    /**
     * The queue of positions.
     */
    val points: Deque<Position> = LinkedList()

    /**
     * Adds a Position onto the queue.
     * @param p The Position to add.
     */
    fun addFirst(p: Position) = points.addFirst(p)

    /**
     * Adds a position onto the queue.
     * @param p The position to add.
     */
    fun addLast(p: Position) = points.addLast(p)

    /**
     * Peeks at the next tile in the path.
     * @return The next tile.
     */
    fun peek(): Position = points.peek()

    /**
     * Polls a position from the path.
     * @return The polled position.
     */
    fun poll(): Position = points.poll()
    val isEmpty: Boolean
        /**
         * Gets if the path is empty.
         * @return If the tile deque is empty.
         */
        get() = points.isEmpty()
}

