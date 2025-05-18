package net.scapeemulator.game.task

class TaskScheduler {
     val tasks: MutableList<Task> = ArrayList()

    fun schedule(task: Task) = tasks.add(task)


    fun tick() {
        val it = tasks.iterator()
        while (it.hasNext()) {
            val task = it.next()
            task.tick()
            if (!task.isRunning) it.remove()
        }
    }
}
