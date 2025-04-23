package net.scapeemulator.game.model

enum class Direction(private val intValue: Int) {
    NONE(-1), NORTH(1), NORTH_EAST(2), EAST(4), SOUTH_EAST(7), SOUTH(6), SOUTH_WEST(5), WEST(3), NORTH_WEST(0);

    fun toInteger(): Int {
        return intValue
    }

    companion object {
        fun between(cur: Position, next: Position): Direction {
            val deltaX = next.x - cur.x
            val deltaY = next.y - cur.y

            if (deltaY == 1) {
                if (deltaX == 1) return Direction.NORTH_EAST
                else if (deltaX == 0) return Direction.NORTH
                else if (deltaX == -1) return Direction.NORTH_WEST
            } else if (deltaY == -1) {
                if (deltaX == 1) return Direction.SOUTH_EAST
                else if (deltaX == 0) return Direction.SOUTH
                else if (deltaX == -1) return Direction.SOUTH_WEST
            } else if (deltaY == 0) {
                if (deltaX == 1) return Direction.EAST
                else if (deltaX == 0) return Direction.NONE
                else if (deltaX == -1) return Direction.WEST
            }

            throw IllegalArgumentException()
        }
    }
}
