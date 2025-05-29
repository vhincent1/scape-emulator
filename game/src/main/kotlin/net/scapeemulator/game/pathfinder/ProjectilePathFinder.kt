package net.scapeemulator.game.pathfinder

import net.scapeemulator.game.model.Position

object ProjectilePathFinder {
    fun find(source: Position, dest: Position): Path {
        return find(source.x, source.y, dest.x, dest.y)
    }

    fun find(sourceX: Int, sourceY: Int, destX: Int, destY: Int): Path {
        val path: Path = Path()
        var curX = sourceX.toDouble()
        var curY = sourceY.toDouble()
        var deltaX = (destX - sourceX).toDouble()
        var deltaY = (destY - sourceY).toDouble()

        while (deltaX >= 1 || deltaY >= 1 || deltaX <= -1 || deltaY <= -1) {
            deltaX = deltaX / 2
            deltaY = deltaY / 2
        }

        var prevX = sourceX
        var prevY = sourceY

        while (true) {
            curX += deltaX
            curY += deltaY

            if (prevX != curX.toInt() || prevY != curY.toInt()) {
                path.addLast(Position(curX.toInt(), curY.toInt()))
                prevX = curX.toInt()
                prevY = curY.toInt()
            }

            if (curX.toInt() == destX && curY.toInt() == destY) {
                return path
            }
        }
    }
}