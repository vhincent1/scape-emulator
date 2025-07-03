package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.model.WalkingQueue
import net.scapeemulator.game.msg.ResetMinimapFlagMessage
import net.scapeemulator.game.msg.WalkMessage
import net.scapeemulator.game.pathfinder.AStarPathFinder

//import net.scapeemulator.game.pathfinder.AStarPathFinder
val WalkMessageHandler = MessageHandler<WalkMessage> { player, message ->
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


val WalkMessageHandler2 = MessageHandler<WalkMessage> { player, message ->
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