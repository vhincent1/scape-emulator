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
import net.scapeemulator.game.model.Direction
import net.scapeemulator.game.model.Position
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * @author Graham Edgecombe
 */
class AStarPathFinder : PathFinder() {
    /**
     * Represents a node used by the A* algorithm.
     *
     * @author Graham Edgecombe
     */
    class Node(val x: Int, val y: Int) : Comparable<Node> {
        var parent: Node? = null
        var cost: Int = 0
        private val heuristic = 0
        private val depth = 0
        override fun hashCode(): Int {
            val prime = 31
            var result = 1
            result = prime * result + cost
            result = prime * result + depth
            result = prime * result + heuristic
            result = prime * result + (if ((parent == null)) 0 else parent.hashCode())
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

        override fun compareTo(other: Node): Int = cost - other.cost
    }

    private var current: Node? = null
    private lateinit var nodes: Array<Array<Node?>>
    private val closed: MutableSet<Node?> = HashSet()
    private val open: MutableSet<Node> = HashSet()

    override fun find(
        position: Position,
        radius: Int,
        srcX: Int,
        srcY: Int,
        dstX: Int,
        dstY: Int,
        objWidth: Int,
        objLength: Int,
        size: Int
    ): Path? {
        /**
         * Okay, the radius @2 is quick but it does not cover the whole map.
         * The larger the radius is times by, the slower this PF becomes.
         */
        var srcX = srcX
        var srcY = srcY
        var dstX = dstX
        var dstY = dstY
        val width = radius * 2
        val length = width * 2

        /* Move our coordinates to the center so we don't run into bounds issues */
        srcX += radius
        srcY += radius
        dstX += radius
        dstY += radius

        if (dstX < 0 || dstY < 0 || dstX >= width || dstY >= length)
            return null // out of range

        val map: TraversalMap = GameServer.WORLD.traversalMap

        open.clear()
        closed.clear()

        nodes = Array(width) { arrayOfNulls(length) }
        for (x in 0 until width)
            for (y in 0 until length)
                nodes[x][y] = Node(x, y)

        open.add(nodes[srcX][srcY]!!)
        while (open.size > 0) {
            current = getLowestCost()
            if (current === nodes[dstX][dstY]) break
            open.remove(current)
            closed.add(current)

            val x = current!!.x
            val y = current!!.y
            val tP = Position(position.x + x - radius, position.y + y - radius, position.height)

            // south
            if (y > 0 && map.isTraversable(Direction.SOUTH, tP, size)) {
                val n = nodes[x][y - 1]
                examineNode(n)
            }
            // west
            if (x > 0 && map.isTraversable(Direction.WEST, tP, size)) {
                val n = nodes[x - 1][y]
                examineNode(n)
            }
            // north
            if (y < length - 1 && map.isTraversable(Direction.NORTH, tP, size)) {
                val n = nodes[x][y + 1]
                examineNode(n)
            }
            // east
            if (x < width - 1 && map.isTraversable(Direction.EAST, tP, size)) {
                val n = nodes[x + 1][y]
                examineNode(n)
            }
            // south west
            if (x > 0 && y > 0 && map.isTraversable(Direction.SOUTH_WEST, tP, size)) {
                val n = nodes[x - 1][y - 1]
                examineNode(n)
            }
            // north west
            if (x > 0 && y < length - 1 && map.isTraversable(Direction.NORTH_WEST, tP, size)) {
                val n = nodes[x - 1][y + 1]
                examineNode(n)
            }
            // south east
            if (x < width - 1 && y > 0 && map.isTraversable(Direction.SOUTH_EAST, tP, size)) {
                val n = nodes[x + 1][y - 1]
                examineNode(n)
            }
            // north east
            if (x < width - 1 && y < length - 1 && map.isTraversable(Direction.NORTH_EAST, tP, size)) {
                val n = nodes[x + 1][y + 1]
                examineNode(n)
            }
        }

        if (nodes[dstX][dstY]?.parent == null) {
            var newX = dstX
            var newY = dstY
            var minDistance = 999
            var minCost = 999999
            for (x in dstX - UNREACHABLE_SEARCH_DISTANCE..dstX + UNREACHABLE_SEARCH_DISTANCE) {
                for (y in dstY - UNREACHABLE_SEARCH_DISTANCE..dstY + UNREACHABLE_SEARCH_DISTANCE) {
                    if ((x >= 0 && x < width) && (y >= 0 && y < length) && (nodes[x][y]?.parent != null)) {
                        println("Unreachable------")
                        var deltaX = 0
                        var deltaY = 0
                        if (y < dstY) {
                            deltaY = dstY - y
                        } else if (y > (dstY + objLength - 1)) {
                            deltaY = y + 1 - (dstY + objLength)
                        }
                        if (x >= dstX) {
                            if (x > (dstX + objWidth - 1)) {
                                deltaX = 1 + (x - dstX - objWidth)
                            }
                        } else {
                            deltaX = dstX - x
                        }
                        val dist = sqrt((deltaX * deltaX + deltaY + deltaY).toDouble()).toInt()
                        val cost = nodes[x][y]?.cost!!
                        if (dist < minDistance || (dist == minDistance && cost < minCost)) {
                            minDistance = dist
                            minCost = cost
                            newX = x
                            newY = y
                        }
                    }
                }
            }
            if (nodes[newX][newY]?.parent == null) {
                println("Still no path")
                return null
            }
            dstX = newX
            dstY = newY
        }

        val path = Path()
        var node: Node? = nodes[dstX][dstY]
        while (node !== nodes[srcX][srcY]) {
            val pos = Position(node!!.x + position.x - radius, node.y + position.y - radius)
            path.addFirst(pos)
            node = node.parent
            //gameItem
        }
        return path
    }

    private fun getLowestCost(): Node? {
        var curLowest: Node? = null
        for (n in open) {
            if (curLowest == null)
                curLowest = n
            else if (n.cost < curLowest.cost)
                curLowest = n
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
     *
     * @param src
     * The source node.
     * @param dst
     * The distance node.
     *
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

        /**
         * The radius to search if we can't find a path to our tile.
         */
        private const val UNREACHABLE_SEARCH_DISTANCE = 10
    }
}
