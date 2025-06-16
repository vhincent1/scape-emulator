package net.scapeemulator.game.model

import java.util.*
import kotlin.math.abs
import kotlin.math.max

//class WalkingQueue(private val mob: Mob) {
//    private val points: Deque<Position> = ArrayDeque<Position>()
//    var isRunningQueue: Boolean = false
//    var isMinimapFlagReset: Boolean = false
//    private var walkDirection: Direction = Direction.NONE
//    private var runDirection: Direction = Direction.NONE
//
//    fun isMoving(): Boolean = walkDirection != Direction.NONE || runDirection != Direction.NONE
//
//    fun reset() {
//        points.clear()
//        this.isRunningQueue = false
//        this.isMinimapFlagReset = true
//    }
//
//    fun addFirstStep(position: Position) {
//        points.clear()
//        this.isRunningQueue = false
//        addStepImpl(position, mob.position)
//    }
//
//    fun addStep(position: Position) {
//        addStepImpl(position, points.peekLast())
//    }
//
//    private fun addStepImpl(position: Position, last: Position) {
//        var deltaX = position.x - last.x
//        var deltaY = position.y - last.y
//        val max = max(abs(deltaX.toDouble()), abs(deltaY.toDouble())).toInt()
//        for (i in 0..<max) {
//            if (deltaX < 0) deltaX++
//            else if (deltaX > 0) deltaX--
//            if (deltaY < 0) deltaY++
//            else if (deltaY > 0) deltaY--
//            points.add(Position(position.x - deltaX, position.y - deltaY, position.height))
//        }
//    }
//
//    fun tick() {
//        var position: Position? = mob.position
//        walkDirection = Direction.NONE
//        runDirection = Direction.NONE
//        var next = points.poll()
//        if (next != null) {
//            walkDirection = Direction.between(position!!, next)
//            position = next
//            if (this.isRunningQueue || mob.isRunning) {
//                next = points.poll()
//                if (next != null) {
//                    runDirection = between(position, next)
//                    position = next
//                }
//            }
//        }
//        mob.setDirections(walkDirection, runDirection)
//        mob.position = position!!
//    }
//}


/**
 * This [WalkingQueue] is associated with a certain [Mob].
 * This should handle the [Deque] for the walking of the associated
 * [Mob]. There is a [Deque] to hold the positions to walk to (head
 * to tail), as well as a [Deque] holding the most recent [Position]
 * points walked to.
 *
 * @author ?
 */
class WalkingQueue(mob: Mob) {
    private val mob: Mob = mob
    private val points: Deque<Position> = ArrayDeque()
    var isRunningQueue: Boolean = false
    var isMinimapFlagReset: Boolean = false
    val recentPoints: Deque<Position> = LinkedList()

    private var walkDirection: Direction = Direction.NONE
    private var runDirection: Direction = Direction.NONE
    fun isMoving(): Boolean = walkDirection != Direction.NONE || runDirection != Direction.NONE

    fun reset() {
        points.clear()
        isRunningQueue = false
        isMinimapFlagReset = true
    }

    fun addPoint(position: Position) {
        points.add(position)
    }

    fun addFirstStep(position: Position) {
        points.clear()
        isRunningQueue = false
        addStepImpl(position, mob.position)
    }

    fun addStep(position: Position) {
        addStepImpl(position, points.peekLast())
    }

    fun peek(): Position {
        return points.peekFirst()
    }

    private fun addStepImpl(position: Position, last: Position) {
        var deltaX: Int = position.x - last.x
        var deltaY: Int = position.y - last.y

        var max = max(abs(deltaX.toDouble()), abs(deltaY.toDouble())).toInt()

        /* Bug fix? */
        if (max < 1) {
            max = 1
        }

        for (i in 0 until max) {
            if (deltaX < 0) {
                deltaX++
            } else if (deltaX > 0) {
                deltaX--
            }

            if (deltaY < 0) {
                deltaY++
            } else if (deltaY > 0) {
                deltaY--
            }

            points.add(Position(position.x - deltaX, position.y - deltaY, position.height))
        }
    }

    /**
     * Perform the tick for the [WalkingQueue]. 1) The associated
     * [Mob] will change position to the first [Position] A, on the
     * [Deque]. The old point will be added to the "most recent" points.
     * [Mob.getFirstDirection] will be adjusted as well. 2) If
     * [Mob.isRunning] or [], do the same for the
     * next point and adjust [Mob.getSecondDirection]. If direction to A
     * is not traversable, performs a [] instead of everything else
     * described.
     */
    fun tick() {
        var position: Position = mob.position
        var firstDirection: Direction? = Direction.NONE
        var secondDirection: Direction? = Direction.NONE

        walkDirection = firstDirection!!
        runDirection = secondDirection!!

        var next = points.poll()

        if (next != null) {
            var direction: Direction = Direction.between(position, next)
            var traversable = !mob.isClipped() || Direction.isTraversable(position, direction, mob.size)
            if (traversable) {
                firstDirection = direction
                addRecentPoint(position)
                position = next
                if (isRunningQueue || mob.isRunning) {
                    next = points.poll()
                    if (next != null) {
                        direction = Direction.between(position, next)
                        traversable = !mob.isClipped() || Direction.isTraversable(position, direction, mob.size)
                        if (traversable) {
                            secondDirection = direction
                            addRecentPoint(position)
                            position = next
                        }
                    }
                }
            }
            if (!traversable) reset()
        }

        mob.setDirections(firstDirection, secondDirection)
        mob.position = position
    }

    /**
     * Add position to the tail of []. When
     * [] has reached its maximum size,
     * [AMOUNT_POINTS] the head will be removed.
     *
     * @param position The [Position] to add to the most recent points
     * [Deque].
     */
    fun addRecentPoint(position: Position) {
        if (recentPoints.size >= WalkingQueue.Companion.AMOUNT_POINTS) {
            recentPoints.poll()
        }
        recentPoints.addLast(position)
    }

    val isEmpty: Boolean
        /**
         * Gets whether we have no [Position] to walk to.
         * @return Checks whether the [Deque] holding the "walk to" points is
         * empty.
         */
        get() = points.isEmpty()

    companion object {
        /* Maximum amount of points in recentPoints. */
        private const val AMOUNT_POINTS = 100
    }
}
