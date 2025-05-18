package net.scapeemulator.game.model

import net.scapeemulator.game.model.Direction.Companion.between
import java.util.*
import kotlin.math.abs
import kotlin.math.max

class WalkingQueue(private val mob: Mob) {
    private val points: Deque<Position> = ArrayDeque<Position>()
    var isRunningQueue: Boolean = false
    var isMinimapFlagReset: Boolean = false

    fun reset() {
        points.clear()
        this.isRunningQueue = false
        this.isMinimapFlagReset = true
    }

    fun addFirstStep(position: Position) {
        points.clear()
        this.isRunningQueue = false
        addStepImpl(position, mob.position)
    }

    fun addStep(position: Position) {
        addStepImpl(position, points.peekLast())
    }

    private fun addStepImpl(position: Position, last: Position) {
        var deltaX = position.x - last.x
        var deltaY = position.y - last.y

        val max = max(abs(deltaX.toDouble()), abs(deltaY.toDouble())).toInt()

        for (i in 0..<max) {
            if (deltaX < 0) deltaX++
            else if (deltaX > 0) deltaX--

            if (deltaY < 0) deltaY++
            else if (deltaY > 0) deltaY--

            points.add(Position(position.x - deltaX, position.y - deltaY, position.height))
        }
    }


    fun tick() {
        var position: Position? = mob.position

        var firstDirection = Direction.NONE
        var secondDirection = Direction.NONE

        var next = points.poll()
        if (next != null) {
            firstDirection = Direction.between(position!!, next)
            position = next

            if (this.isRunningQueue || mob.isRunning) {
                next = points.poll()
                if (next != null) {
                    secondDirection = between(position, next)
                    position = next
                }
            }
        }

        mob.setDirections(firstDirection, secondDirection)
        mob.position = position!!
    }
}
