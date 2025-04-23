package net.scapeemulator.game.model

import net.scapeemulator.game.task.Action

class EmoteAction(player: Player, animation: Int, spotAnimation: Int, delay: Int) :
    Action<Player>(player, delay, false) {
    constructor(player: Player, animation: Int, delay: Int) : this(player, animation, -1, delay)

    init {
        player.walkingQueue.reset()
        player.playAnimation(Animation(animation))
        if (spotAnimation != -1) player.playSpotAnimation(SpotAnimation(spotAnimation))
    }

    public override fun execute() {
        stop()
    }

    companion object {
        const val YES: Int = 855
        const val NO: Int = 856
        const val BOW: Int = 858
        const val ANGRY: Int = 859
        const val THINK: Int = 857
        const val WAVE: Int = 863
        const val SHRUG: Int = 2113
        const val CHEER: Int = 862
        const val BECKON: Int = 864
        const val JUMP_FOR_JOY: Int = 2109
        const val LAUGH: Int = 861
        const val YAWN: Int = 2111
        const val DANCE: Int = 866
        const val JIG: Int = 2106
        const val SPIN: Int = 2107
        const val HEADBANG: Int = 2108
        const val CRY: Int = 860
        const val BLOW_KISS: Int = 1368
        const val BLOW_KISS_GRAPHIC: Int = 574
        const val PANIC: Int = 2105
        const val RASPBERRY: Int = 2110
        const val CLAP: Int = 865
        const val SALUTE: Int = 2112
    }
}
