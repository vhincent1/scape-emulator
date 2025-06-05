package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.PATHFINDING_ENABLED
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.model.WalkingQueue
import net.scapeemulator.game.msg.ResetMinimapFlagMessage
import net.scapeemulator.game.msg.WalkMessage
import net.scapeemulator.game.pathfinder.AStarPathFinder
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.SmartPathFinder

//import net.scapeemulator.game.pathfinder.AStarPathFinder

private val WalkMessageHandlerDefault = MessageHandler<WalkMessage> { player, message ->
    val z = player.position.height
    val queue = player.walkingQueue
    val steps = message.steps
    var position = Position(steps[0]!!.x, steps[0]!!.y, z)
    queue.addFirstStep(position)
    queue.isRunningQueue = message.isRunning // must be after first step which reset()s
    for (i in 1..<steps.size) {
        position = Position(steps[i]!!.x, steps[i]!!.y, z)
        queue.addStep(position)
    }
    player.stopAction()
}

//TODO fix
private val WalkMessageHandlerPF = MessageHandler<WalkMessage> { player, message ->
    val z: Int = player.position.height

    val queue: WalkingQueue = player.walkingQueue

    val destination = Position(message.destination.x, message.destination.y, z)
    val position: Position = player.position

    val baseLocalX = position.getBaseLocalX()
    val baseLocalY = position.getBaseLocalY()
    val destLocalX: Int = destination.x - baseLocalX
    val destLocalY: Int = destination.y - baseLocalY

    val base = Position(baseLocalX, baseLocalY, position.height)
    val pathFinder = AStarPathFinder()
    val path = pathFinder.find(base, 104, position.getLocalX(), position.getLocalY(), destLocalX, destLocalY)

//    val route = smartRoute(position.x, position.y, destination.x, destination.y, z)
//
//    if (route.success) {
//        println("Can walk")
//    } else {
//        println("CANT!")
//    }

    if (path != null) {
        val first = path.poll()

        queue.addFirstStep(first)
        queue.isRunningQueue = message.isRunning
        player.stopAction()

        while (path.points.isNotEmpty()) {
            val step = path.poll()
            queue.addStep(step)
        }
    } else {
//        player.send(ResetMinimapFlagMessage(message.minimapFlag))
        player.send(ResetMinimapFlagMessage())
        queue.reset()
    }
}

internal val WalkMessageHandler = if (PATHFINDING_ENABLED) WalkMessageHandlerPF else WalkMessageHandlerDefault

fun smartRoute(srcX: Int, srcY: Int, destX: Int, destY: Int, level: Int): Route {
    val pf = SmartPathFinder()
    val flags = clipFlags(srcX, srcY, level, pf.searchMapSize)
    return pf.findPath(flags, srcX, srcY, destX, destY)
}

fun clipFlags(centerX: Int, centerY: Int, level: Int, size: Int): IntArray {
    val half = size / 2
    val flags = IntArray(size * size)
    val rangeX = centerX - half until centerX + half
    val rangeY = centerY - half until centerY + half

    println("COORDINATES CENTER: $centerX $centerY $level")
    println("COORDINATES RANGE: $rangeX $rangeY $level")
    for (y in rangeY) {
        for (x in rangeX) {
            val coords = Position(x, y, level)
            /*
             * collision map stores tile collision flags for all
             * tiles in the world.
             */
            println("COORDINATES: ${coords.x} ${coords.y} ${coords.height}")
            val region = GameServer.INSTANCE.world.regionManager.getRegion(x, y)

            val flag = region?.getTile(level, x, y)?.flags
            if (flag == null) {
                println("Null flag")
                return intArrayOf()
            }
            println("Flag: $flag")

//            val flag = collisionMap.get(coords)

            val lx = x - (centerX - half)
            val ly = y - (centerY - half)
            val index = (ly * size) + lx
            flags[index] = flag
        }
    }
    return flags
}