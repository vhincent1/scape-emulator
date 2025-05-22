package net.scapeemulator.game.model

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.task.Action
import java.util.*

abstract class Mob() : Entity() {
    @JvmField
    var index: Int = 0
    var isTeleporting: Boolean = false
        protected set

    @JvmField
    val walkingQueue: WalkingQueue = WalkingQueue(this)
    var firstDirection: Direction = Direction.NONE
    var secondDirection: Direction = Direction.NONE
        protected set
    var mostRecentDirection: Direction = Direction.SOUTH
//        protected set

    @JvmField
    val skillSet: SkillSet = SkillSet()
    var animation: Animation? = null
        protected set
    var spotAnimation: SpotAnimation? = null
        protected set

    var hitQueue: Queue<Hit> = LinkedList()
    var primaryHit: Hit? = null
    var secondaryHit: Hit? = null

    protected var action: Action<*>? = null

    fun resetId() {
        this.index = 0
    }

    val isActive: Boolean
        get() = index != 0

    fun startAction(action: Action<*>) {
        if (this.action != null) {
            if (this.action == action) return
            stopAction()
        }
        this.action = action
        //todo

        GameServer.INSTANCE!!.world.taskScheduler.schedule(action)
    }

    fun stopAction() {
        if (action != null) {
            val oldAction: Action<*> = action as Action<*>
            action = null
            oldAction.stop()
        }
    }

    fun teleport(position: Position?) {
        this.position = position!!
        this.isTeleporting = true
        walkingQueue.reset()
    }

    fun setDirections(firstDirection: Direction, secondDirection: Direction) {
        this.firstDirection = firstDirection
        this.secondDirection = secondDirection

        if (secondDirection != Direction.NONE) mostRecentDirection = secondDirection
        else if (firstDirection != Direction.NONE) mostRecentDirection = firstDirection
    }

    val isAnimationUpdated: Boolean
        get() = animation != null

    val isSpotAnimationUpdated: Boolean
        get() = spotAnimation != null

    var isHitUpdated = false

    //        get() = primaryHit != null
    var isHit2Updated = false
//        get() = secondaryHit != null

    var face: Mob? = null
        set(value) {
            field = value
            isFacingUpdated = true
        }
    var isFacingUpdated: Boolean = false

    fun playAnimation(animation: Animation?) {
        this.animation = animation
    }

    fun playSpotAnimation(spotAnimation: SpotAnimation) {
        this.spotAnimation = spotAnimation
    }

    open fun reset() {
        animation = null
        spotAnimation = null
        isTeleporting = false
        walkingQueue.isMinimapFlagReset = false
//        primaryHit = null
//        secondaryHit = null
        hitQueue.clear()
        isHitUpdated = false
        isHit2Updated = false
        isFacingUpdated = false
    }

    abstract val isRunning: Boolean
    abstract fun login()
    abstract fun logout()
    abstract fun getClientIndex(): Int
}
