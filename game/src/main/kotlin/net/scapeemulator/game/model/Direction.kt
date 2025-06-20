package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.pathfinder.Tile
import net.scapeemulator.game.pathfinder.TraversalMap
import java.util.*

enum class Direction(private val intValue: Int, val x: Int, val y: Int, vararg traversal: Int) {
    NONE(-1, 0, 0),
    NORTH(1, 0, 1, Tile.PREVENT_NORTH),
    NORTH_EAST(2, 1, 1, Tile.PREVENT_EAST, Tile.PREVENT_NORTH, Tile.PREVENT_NORTHEAST),
    EAST(4, 1, 0, Tile.PREVENT_EAST),
    SOUTH_EAST(7, 1, -1, Tile.PREVENT_EAST, Tile.PREVENT_SOUTH, Tile.PREVENT_SOUTHEAST),
    SOUTH(6, 0, -1, Tile.PREVENT_SOUTH),
    SOUTH_WEST(5, -1, -1, Tile.PREVENT_WEST, Tile.PREVENT_SOUTH, Tile.PREVENT_SOUTHWEST),
    WEST(3, -1, 0, Tile.PREVENT_WEST),
    NORTH_WEST(0, -1, 1, Tile.PREVENT_WEST, Tile.PREVENT_NORTH, Tile.PREVENT_NORTHWEST);

    fun getInverted(): Direction = when (this) {
        NONE -> NONE
        NORTH -> SOUTH
        NORTH_EAST -> SOUTH_WEST
        EAST -> WEST
        SOUTH_EAST -> NORTH_WEST
        SOUTH -> NORTH
        SOUTH_WEST -> NORTH_EAST
        WEST -> EAST
        NORTH_WEST -> SOUTH_EAST
        else -> throw RuntimeException()
    }

    fun toInteger(): Int = intValue

    companion object {
        fun getNearbyTraversableTiles(from: Position, size: Int): List<Position> {
            val traversalMap: TraversalMap = GameServer.INSTANCE.world.traversalMap
            val positions: MutableList<Position> = LinkedList()
            if (traversalMap.isTraversable(NORTH, from, size))
                positions.add(Position(from.x, from.y + 1, from.height))
            if (traversalMap.isTraversable(SOUTH, from, size))
                positions.add(Position(from.x, from.y - 1, from.height))
            if (traversalMap.isTraversable(EAST, from, size))
                positions.add(Position(from.x + 1, from.y, from.height))
            if (traversalMap.isTraversable(WEST, from, size))
                positions.add(Position(from.x - 1, from.y, from.height))
            if (traversalMap.isTraversable(NORTH_EAST, from, size))
                positions.add(Position(from.x + 1, from.y + 1, from.height))
            if (traversalMap.isTraversable(NORTH_WEST, from, size))
                positions.add(Position(from.x - 1, from.y + 1, from.height))
            if (traversalMap.isTraversable(SOUTH_EAST, from, size))
                positions.add(Position(from.x + 1, from.y - 1, from.height))
            if (traversalMap.isTraversable(SOUTH_WEST, from, size))
                positions.add(Position(from.x - 1, from.y - 1, from.height))
            return positions
        }

        fun isTraversable(from: Position, direction: Direction, size: Int): Boolean {
            val traversalMap: TraversalMap = GameServer.INSTANCE.world.traversalMap
            return traversalMap.isTraversable(direction, from, size)
        }

        fun projectileClipping(from: Position, to: Position): Boolean {
            val traversalMap: TraversalMap = GameServer.INSTANCE.world.traversalMap
            val direction = between(from, to)
            return traversalMap.isTraversable(direction, from)
        }

        fun between(cur: Position, next: Position): Direction {
            val deltaX: Int = next.x - cur.x
            val deltaY: Int = next.y - cur.y
            if (deltaY >= 1) {
                if (deltaX >= 1) return NORTH_EAST
                else if (deltaX == 0) return NORTH
                else if (deltaX <= -1) return NORTH_WEST
            } else if (deltaY <= -1) {
                if (deltaX >= 1) return SOUTH_EAST
                else if (deltaX == 0) return SOUTH
                else if (deltaX <= -1) return SOUTH_WEST
            } else if (deltaY == 0) {
                if (deltaX >= 1) return EAST
                else if (deltaX == 0) return NONE
                else if (deltaX <= -1) return WEST
            }
            throw IllegalArgumentException("$deltaX $deltaY")
        }
    }
}