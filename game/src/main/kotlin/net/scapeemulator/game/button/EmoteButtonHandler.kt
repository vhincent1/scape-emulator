package net.scapeemulator.game.button

import net.scapeemulator.game.model.EmoteAction
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player

class EmoteButtonHandler : ButtonHandler(Interface.EMOTES) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        when (slot) {
            2 -> {
                player.startAction(EmoteAction(player, EmoteAction.YES, 4))
            }
            3 -> {
                player.startAction(EmoteAction(player, EmoteAction.NO, 4))
            }
            4 -> {
                player.startAction(EmoteAction(player, EmoteAction.BOW, 4))
            }
            5 -> {
                player.startAction(EmoteAction(player, EmoteAction.ANGRY, 4))
            }
            6 -> {
                player.startAction(EmoteAction(player, EmoteAction.THINK, 4))
            }
            7 -> {
                player.startAction(EmoteAction(player, EmoteAction.WAVE, 4))
            }
            8 -> {
                player.startAction(EmoteAction(player, EmoteAction.SHRUG, 3))
            }
            9 -> {
                player.startAction(EmoteAction(player, EmoteAction.CHEER, 5))
            }
            10 -> {
                player.startAction(EmoteAction(player, EmoteAction.BECKON, 3))
            }
            12 -> {
                player.startAction(EmoteAction(player, EmoteAction.LAUGH, 4))
            }
            11 -> {
                player.startAction(EmoteAction(player, EmoteAction.JUMP_FOR_JOY, 3))
            }
            13 -> {
                player.startAction(EmoteAction(player, EmoteAction.YAWN, 6))
            }
            14 -> {
                player.startAction(EmoteAction(player, EmoteAction.DANCE, 8))
            }
            15 -> {
                player.startAction(EmoteAction(player, EmoteAction.JIG, 7))
            }
            16 -> {
                player.startAction(EmoteAction(player, EmoteAction.SPIN, 3))
            }
            17 -> {
                player.startAction(EmoteAction(player, EmoteAction.HEADBANG, 7))
            }
            18 -> {
                player.startAction(EmoteAction(player, EmoteAction.CRY, 5))
            }
            19 -> {
                player.startAction(EmoteAction(player, EmoteAction.BLOW_KISS, EmoteAction.BLOW_KISS_GRAPHIC, 11))
            }
            20 -> {
                player.startAction(EmoteAction(player, EmoteAction.PANIC, 4))
            }
            21 -> {
                player.startAction(EmoteAction(player, EmoteAction.RASPBERRY, 5))
            }
            22 -> {
                player.startAction(EmoteAction(player, EmoteAction.CLAP, 6))
            }
            23 -> {
                player.startAction(EmoteAction(player, EmoteAction.SALUTE, 3))
            }
            24 -> {
                player.startAction(EmoteAction(player, EmoteAction.GOBLIN_BOW, 3));
            }
            25 -> {
                player.startAction(EmoteAction(player, EmoteAction.GOBLIN_SALUTE, 3));
            }
            26 -> {
                player.startAction(EmoteAction(player, EmoteAction.GLASS_BOX, 3));
            }
            27 -> {
                player.startAction(EmoteAction(player, EmoteAction.CLIMB_ROPE, 3));
            }
            28 -> {
                player.startAction(EmoteAction(player, EmoteAction.LEAN, 3));
            }
            29 -> {
                player.startAction(EmoteAction(player, EmoteAction.GLASS_WALL, 3));
            }
            30 -> {
                player.startAction(EmoteAction(player, EmoteAction.IDEA, 3));
            }
            31 -> {
                player.startAction(EmoteAction(player, EmoteAction.STOMP, 3));
            }
            32 -> {
                player.startAction(EmoteAction(player, EmoteAction.FLAP, 3));
            }
            33 -> {
                player.startAction(EmoteAction(player, EmoteAction.SLAP_HEAD, 3));
            }
            34 -> {
                player.startAction(EmoteAction(player, EmoteAction.ZOMBIE_WALK, 3));
            }
            35 -> {
                player.startAction(EmoteAction(player, EmoteAction.ZOMBIE_DANCE, 3));
            }
            36 -> {
                player.startAction(EmoteAction(player, EmoteAction.ZOMBIE_HAND, EmoteAction.ZOMBIE_HAND_GFX, 3));
            }
            37 -> {
                player.startAction(EmoteAction(player, EmoteAction.SCARED, 3));
            }
            38 -> {
                player.startAction(EmoteAction(player, EmoteAction.BUNNY_HOP, 3));
            }
            39 -> {
                //TODO: skillcape
                player.sendMessage("You need to be wearing a skillcape to perform this emote.");
            }
            40 -> {
                player.startAction(EmoteAction(player, EmoteAction.SNOWMAN_DANCE, 3));
            }
            41 -> {
                player.startAction(EmoteAction(player, EmoteAction.AIR_GUITAR, EmoteAction.AIR_GUITAR_GFX, 3));
            }
            42 -> {
                player.startAction(EmoteAction(player, EmoteAction.SAFETY_FIRST, EmoteAction.SAFETY_FIRST_GFX, 3));
            }
            43 -> {
                player.startAction(EmoteAction(player, EmoteAction.EXPLORE, EmoteAction.EXPLORE_GFX, 3));
            }
            44 -> {
                player.startAction(EmoteAction(player, EmoteAction.TRICK, EmoteAction.TRICK_GFX, 3));
            }
            45 -> {
                player.startAction(EmoteAction(player, EmoteAction.FREEZE, EmoteAction.FREEZE_GFX, 3));
            }
            46 -> {
                //TODO: Fix give thanks, gfx is wrong and it does not transform into turkey?
                player.startAction(EmoteAction(player, 10996, 1714, 3));
            }
            47 -> {
                player.startAction(EmoteAction(player, EmoteAction.AROUND_WORLD, EmoteAction.AROUND_WORLD_GFX, 3));
            }
        }
    }
}
