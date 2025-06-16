package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.msg.WalkMessage

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
        println("Adding step $position")
    }
    player.stopAction()
}


internal val WalkMessageHandler = WalkMessageHandlerDefault