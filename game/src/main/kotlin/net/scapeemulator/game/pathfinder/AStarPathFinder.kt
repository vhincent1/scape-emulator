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

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.model.Position
import kotlin.math.abs

/**
 * @author Graham Edgecombe
 */
class AStarPathFinder : PathFinder() {
    /**
     * Represents a node used by the A* algorithm.
     * @author Graham Edgecombe
     */
    class Node
    /**
     * Creates a node.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */(
        /**
         * The x coordinate.
         */
        val x: Int,
        /**
         * The y coordinate.
         */
        val y: Int
    ) : Comparable<Node?> {
        /**
         * Gets the parent node.
         * @return The parent node.
         */
        /**
         * Sets the parent.
         * @param parent The parent.
         */
        /**
         * The parent node.
         */
        var parent: Node? = null

        /**
         * The cost.
         */
        var cost: Int = 0

        /**
         * The heuristic.
         */
        private val heuristic = 0

        /**
         * The depth.
         */
        private val depth = 0

        /**
         * Gets the X coordinate.
         * @return The X coordinate.
         */

        /**
         * Gets the Y coordinate.
         * @return The Y coordinate.
         */

        override fun hashCode(): Int {
            val prime = 31
            var result = 1
            result = prime * result + cost
            result = prime * result + depth
            result = prime * result + heuristic
            result = (prime * result
                    + (if ((parent == null)) 0 else parent.hashCode()))
            result = prime * result + x
            result = prime * result + y
            return result
        }

        override fun equals(obj: Any?): Boolean {
            if (this === obj) return true
            if (obj == null) return false
            if (javaClass != obj.javaClass) return false
            val other = obj as Node
            if (cost != other.cost) return false
            if (depth != other.depth) return false
            if (heuristic != other.heuristic) return false
            if (parent == null) {
                if (other.parent != null) return false
            } else if (parent != other.parent) return false
            if (x != other.x) return false
            if (y != other.y) return false
            return true
        }

        override fun compareTo(arg0: Node?): Int {
            return cost - arg0!!.cost
        }
    }

    private var current: Node? = null
    private lateinit var nodes: Array<Array<Node?>>
    private val closed: MutableSet<Node?> = HashSet()
    private val open: MutableSet<Node> = HashSet()

    override fun find(
        position: Position,
        width: Int,
        length: Int,
        srcX: Int,
        srcY: Int,
        dstX: Int,
        dstY: Int,
        size: Int
    ): Path? {
        if (dstX < 0 || dstY < 0 || dstX >= width || dstY >= length) {
            return null // out of range
        }

        val map = GameServer.WORLD.traversalMap//World.getWorld().getTraversalMap()

        nodes = Array(width) { arrayOfNulls(length) }
        for (x in 0 until width) {
            for (y in 0 until length) {
                nodes[x][y] = Node(x, y)
            }
        }

        open.add(nodes[srcX][srcY]!!)

        while (open.size > 0) {
            current = lowestCost
            if (current === nodes[dstX][dstY]) {
                break
            }
            open.remove(current)
            closed.add(current)

            val x = current!!.x
            val y = current!!.y

            // south
            if (y > 0 && map.isTraversableSouth(position.height, position.x + x, position.y + y, size)) {
                val n = nodes[x][y - 1]
                examineNode(n)
            }
            // west
            if (x > 0 && map.isTraversableWest(position.height, position.x + x, position.y + y, size)) {
                val n = nodes[x - 1][y]
                examineNode(n)
            }
            // north
            if (y < length - 1 && map.isTraversableNorth(
                    position.height,
                    position.x + x,
                    position.y + y,
                    size
                )
            ) {
                val n = nodes[x][y + 1]
                examineNode(n)
            }
            // east
            if (x < width - 1 && map.isTraversableEast(
                    position.height,
                    position.x + x,
                    position.y + y,
                    size
                )
            ) {
                val n = nodes[x + 1][y]
                examineNode(n)
            }
            // south west
            if (x > 0 && y > 0 && map.isTraversableSouthWest(
                    position.height,
                    position.x + x,
                    position.y + y,
                    size
                )
            ) {
                val n = nodes[x - 1][y - 1]
                examineNode(n)
            }
            // north west
            if (x > 0 && y < length - 1 && map.isTraversableNorthWest(
                    position.height,
                    position.x + x,
                    position.y + y,
                    size
                )
            ) {
                val n = nodes[x - 1][y + 1]
                examineNode(n)
            }

            // south east
            if (x < width - 1 && y > 0 && map.isTraversableSouthEast(
                    position.height,
                    position.x + x,
                    position.y + y,
                    size
                )
            ) {
                val n = nodes[x + 1][y - 1]
                examineNode(n)
            }
            // north east
            if (x < width - 1 && y < length - 1 && map.isTraversableNorthEast(
                    position.height,
                    position.x + x,
                    position.y + y,
                    size
                )
            ) {
                val n = nodes[x + 1][y + 1]
                examineNode(n)
            }
        }

        if (nodes[dstX][dstY]?.parent == null) {
            return null
        }

        val p: Path = Path()
        var n: Node? = nodes[dstX][dstY]
        while (n !== nodes[srcX][srcY] && n != null) {
            p.addFirst(Position(n.x + position.x, n.y + position.y))
            n = n.parent
        }

        return p
    }

    private val lowestCost: Node?
        get() {
            var curLowest: Node? = null
            for (n in open) {
                if (curLowest == null) {
                    curLowest = n
                } else {
                    if (n.cost < curLowest.cost) {
                        curLowest = n
                    }
                }
            }
            return curLowest
        }

    private fun examineNode(n: Node?) {
        if (n == null) return
        val heuristic = estimateDistance(current, n)
        val nextStepCost = current!!.cost + heuristic
        if (nextStepCost < n.cost) {
            open.remove(n)
            closed.remove(n)
        }
        if (!open.contains(n) && !closed.contains(n)) {
            n.parent = current
            n.cost = nextStepCost
            open.add(n)
        }
    }

    /**
     * Estimates a distance between the two points.
     * @param src The source node.
     * @param dst The distance node.
     * @return The distance.
     */
    fun estimateDistance(src: Node?, dst: Node): Int {
        val deltaX = src!!.x - dst.x
        val deltaY = src.y - dst.y
        return ((abs(deltaX.toDouble()) + abs(deltaY.toDouble())) * COST_STRAIGHT).toInt()
    }

    companion object {
        /**
         * The cost of moving in a straight line.
         */
        private const val COST_STRAIGHT = 10
    }

}
