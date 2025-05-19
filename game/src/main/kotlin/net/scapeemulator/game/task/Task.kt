package net.scapeemulator.game.task

abstract class Task(delay: Int, immediate: Boolean) {
    var isRunning: Boolean = true
        private set
    val delay: Int
    private var countdown: Int

    init {
        require(delay >= 1)
        this.delay = delay
        this.countdown = if (immediate) 1 else delay
    }

    abstract fun execute()

    fun tick() {
        if (--countdown == 0) {
            countdown = delay
            execute()
        }
    }

    open fun stop() {
        isRunning = false
    }
}
