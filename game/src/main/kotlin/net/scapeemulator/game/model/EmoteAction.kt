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

    override fun execute() {
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
        const val GOBLIN_BOW: Int = 2127
        const val GOBLIN_SALUTE: Int = 2128
        const val GLASS_BOX: Int = 1131
        const val CLIMB_ROPE: Int = 1130
        const val LEAN: Int = 1129
        const val GLASS_WALL: Int = 1128
        const val IDEA: Int = 4275
        const val STOMP: Int = 1745
        const val FLAP: Int = 4280
        const val SLAP_HEAD: Int = 4276
        const val ZOMBIE_WALK: Int = 3544
        const val ZOMBIE_DANCE: Int = 3543
        const val ZOMBIE_HAND: Int = 7272
        const val ZOMBIE_HAND_GFX: Int = 1244
        const val SCARED: Int = 2836
        const val BUNNY_HOP: Int = 6111
        const val SNOWMAN_DANCE: Int = 7531
        const val AIR_GUITAR: Int = 2414
        const val AIR_GUITAR_GFX: Int = 1537
        const val SAFETY_FIRST: Int = 8770
        const val SAFETY_FIRST_GFX: Int = 1553
        const val EXPLORE: Int = 9990
        const val EXPLORE_GFX: Int = 1734
        const val TRICK: Int = 10530
        const val TRICK_GFX: Int = 1864
        const val FREEZE: Int = 11044
        const val FREEZE_GFX: Int = 1973
        const val AROUND_WORLD: Int = 11542
        const val AROUND_WORLD_GFX: Int = 2037
    }
}
