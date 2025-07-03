//package net.scapeemulator.game.model
//
//import net.scapeemulator.game.pathfinder.Path
//import net.scapeemulator.game.task.Action
//import java.util.*
//
//
//class FollowAction(
//    mob: Mob,
//    target: Mob,
//    private val behind: Boolean,
//    protected var distance: Int
//) :
//    Action<Mob>(mob, 1, true) {
//    protected val target: Mob = target
//    private var path: Path? = Path()
//
//    constructor(mob: Mob, target: Mob, behind: Boolean) : this(mob, target, behind, 0) {
//        require(behind) { "Must state a max distance if not following behind." }
//    }
//
//    init {
//        mob.faceMob = target
//        mob.walkingQueue.reset()
//    }
//
//    override fun execute() {
//        if (target.isTeleporting || !target.position.isWithinDistance(mob.position)) {
//            stop()
//            return
//        }
//
//        var targetBounds: Area = target.getBounds()
//        if (behind) {
//            val recentPoints: Deque<Position> = target.walkingQueue.getRecentPoints()
//
//            if (!recentPoints.isEmpty()) {
//                val last = recentPoints.peekLast()
//                val lastX: Int = last.x
//                val lastY: Int = last.y
//                targetBounds = QuadArea(lastX, lastY, lastX + target.size - 1, lastY + target.size - 1)
//            }
//        }
//
//        // Check if we are inside the mob (and not supposed to be)
//        if (distance > 0 && mob.getBounds().anyWithinArea(target.position, target.size, 0, true)) {
//            val p: Position =
//                Direction.getNearbyTraversableTiles(mob.position, mob.size).get(0)
//            mob.walkingQueue.addPoint(p)
//            return
//        }
//
//        // Check if we are inside the target area (don't move)
//        if (targetBounds.anyWithinArea(mob.position, mob.size, distance, false)) {
//            return
//        }
//
//        path = net.scapeemulator.game.pathfinder.DumbPathFinder.find(mob.position, targetBounds.center(), mob.size, 2, false)
//        if (path == null || path.isEmpty) {
//            return
//        }
//        mob.walkingQueue.addPoint(path.poll())
//        if (path.isEmpty) return
//        if (mob is Player) {
//            if (!mob.settings.running) {
//                return
//            }
//            if ((targetBounds.anyWithinArea(
//                    path.peek(), mob.size,
//                    if (distance == 0) 0 else distance - 1, false
//                ) && !behind)
//            ) {
//                return
//            }
//
//            mob.walkingQueue.addPoint(path.poll())
//        }
//    }
//
//    override fun stop() {
////        mob.resetTurnToTarget()
//        super.stop()
//    }
//}