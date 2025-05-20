package net.scapeemulator.game.task

import java.util.concurrent.locks.ReentrantLock

//class TaskScheduler2 {
//    val tasks: MutableList<Task> = ArrayList()
//    fun schedule(task: Task) = tasks.add(task)
//    fun tick() {
//        val it = tasks.iterator()
//        while (it.hasNext()) {
//            val task = it.next()
//            task.tick()
//            if (!task.isRunning) it.remove()
//        }
//    }
//}

class TaskScheduler {
    private val lock = ReentrantLock()
    private val tasks: MutableList<Task> = ArrayList()

    fun schedule(task: Task) {
        lock.lock()
        try {
            tasks.add(task)
        } finally {
            lock.unlock()
        }
    }

    fun tick() {
        lock.lock()
        var taskz: ArrayList<Task?>?
        try {
            taskz = ArrayList(tasks)
        } finally {
            lock.unlock()
        }
        for (task in taskz) {
            if (task == null) continue
            try {
                task.tick()
                if (!task.isRunning) tasks.remove(task)
            } catch (t: Throwable) {
                t.printStackTrace()
                tasks.remove(task)
            }
        }
        taskz.clear()
    }

}