package net.scapeemulator.game.task

import net.scapeemulator.game.model.Mob

abstract class Action<T : Mob>(val mob: T, delay: Int, immediate: Boolean) :
    Task(delay, immediate) {
    override fun stop() {
        super.stop()
        mob.stopAction()
    }
}

fun <T : Mob> Action(mob: T, delay: Int, immediate: Boolean, block: (T, Int, Boolean) -> Unit): Action<T> {
    return object : Action<T>(mob, delay, immediate) {
        override fun execute() {
            block(mob, delay, immediate)
        }
    }
}