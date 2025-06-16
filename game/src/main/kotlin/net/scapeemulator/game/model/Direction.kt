package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.pathfinder.TraversalMap
import java.util.*

enum class Direction(private val intValue: Int, private val x: Int, private val y: Int) {
    NONE(-1, 0, 0),
    NORTH(1, 0, 1),
    NORTH_EAST(2, 1, 1),
    EAST(4, 1, 0),
    SOUTH_EAST(7, 1, -1),
    SOUTH(6, 0, -1),
    SOUTH_WEST(5, -1, -1),
    WEST(3, -1, 0),
    NORTH_WEST(0, -1, 1);

    fun getInverted(): Direction {
        return when (this) {
            NONE -> NONE
            NORTH -> SOUTH
            NORTH_EAST -> SOUTH_WEST
            EAST -> WEST
            SOUTH_EAST -> NORTH_WEST
            SOUTH -> NORTH
            SOUTH_WEST -> NORTH_EAST
            WEST -> EAST
            NORTH_WEST -> SOUTH_EAST
        }
        throw RuntimeException()
    }

    fun toInteger(): Int {
        return intValue
    }

    fun getX(): Int {
        return x
    }

    fun getY(): Int {
        return y
    }

    companion object {
        fun getNearbyTraversableTiles(from: Position, size: Int): List<Position> {
            val traversalMap: TraversalMap = GameServer.INSTANCE.world.traversalMap
            val positions: MutableList<Position> = LinkedList()

            if (traversalMap.isTraversableNorth(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x, from.y + 1, from.height))
            }

            if (traversalMap.isTraversableSouth(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x, from.y - 1, from.height))
            }

            if (traversalMap.isTraversableEast(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x + 1, from.y, from.height))
            }

            if (traversalMap.isTraversableWest(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x - 1, from.y, from.height))
            }

            if (traversalMap.isTraversableNorthEast(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x + 1, from.y + 1, from.height))
            }

            if (traversalMap.isTraversableNorthWest(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x - 1, from.y + 1, from.height))
            }

            if (traversalMap.isTraversableSouthEast(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x + 1, from.y - 1, from.height))
            }

            if (traversalMap.isTraversableSouthWest(from.height, from.x, from.y, size)) {
                positions.add(Position(from.x - 1, from.y - 1, from.height))
            }

            return positions
        }

        fun isTraversable(from: Position, direction: Direction, size: Int): Boolean {
            val traversalMap: TraversalMap = GameServer.INSTANCE.world.traversalMap

            println("Is traversable: $direction $size")
            return when (direction) {
                NORTH -> traversalMap.isTraversableNorth(from.height, from.x, from.y, size)
                SOUTH -> traversalMap.isTraversableSouth(from.height, from.x, from.y, size)
                EAST -> traversalMap.isTraversableEast(from.height, from.x, from.y, size)
                WEST -> traversalMap.isTraversableWest(from.height, from.x, from.y, size)
                NORTH_EAST -> traversalMap.isTraversableNorthEast(from.height, from.x, from.y, size)
                NORTH_WEST -> traversalMap.isTraversableNorthWest(from.height, from.x, from.y, size)
                SOUTH_EAST -> traversalMap.isTraversableSouthEast(from.height, from.x, from.y, size)
                SOUTH_WEST -> traversalMap.isTraversableSouthWest(from.height, from.x, from.y, size)
                NONE -> true
                else -> throw RuntimeException("unknown type")
            }
        }

        fun projectileClipping(from: Position, to: Position): Boolean {
            val traversalMap: TraversalMap = GameServer.INSTANCE.world.traversalMap
            val direction = between(from, to)
            return when (direction) {
                NORTH -> traversalMap.isTraversableNorth(from.height, from.x, from.y, true)
                SOUTH -> traversalMap.isTraversableSouth(from.height, from.x, from.y, true)
                EAST -> traversalMap.isTraversableEast(from.height, from.x, from.y, true)
                WEST -> traversalMap.isTraversableWest(from.height, from.x, from.y, true)
                NORTH_EAST -> traversalMap.isTraversableNorthEast(from.height, from.x, from.y, true)
                NORTH_WEST -> traversalMap.isTraversableNorthWest(from.height, from.x, from.y, true)
                SOUTH_EAST -> traversalMap.isTraversableSouthEast(from.height, from.x, from.y, true)
                SOUTH_WEST -> traversalMap.isTraversableSouthWest(from.height, from.x, from.y, true)
                NONE -> true
                else -> throw RuntimeException("unknown type")
            }
        }

        fun between(cur: Position, next: Position): Direction {
            val deltaX: Int = next.x - cur.x
            val deltaY: Int = next.y - cur.y

            if (deltaY >= 1) {
                if (deltaX >= 1) {
                    return NORTH_EAST
                } else if (deltaX == 0) {
                    return NORTH
                } else if (deltaX <= -1) {
                    return NORTH_WEST
                }
            } else if (deltaY <= -1) {
                if (deltaX >= 1) {
                    return SOUTH_EAST
                } else if (deltaX == 0) {
                    return SOUTH
                } else if (deltaX <= -1) {
                    return SOUTH_WEST
                }
            } else if (deltaY == 0) {
                if (deltaX >= 1) {
                    return EAST
                } else if (deltaX == 0) {
                    return NONE
                } else if (deltaX <= -1) {
                    return WEST
                }
            }
            throw IllegalArgumentException("$deltaX $deltaY")
        }
    }
}
