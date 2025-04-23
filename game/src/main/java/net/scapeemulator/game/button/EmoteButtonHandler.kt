package net.scapeemulator.game.button

import net.scapeemulator.game.model.EmoteAction
import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player

class EmoteButtonHandler : ButtonHandler(Interface.EMOTES) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        if (slot == 2) {
            player.startAction(EmoteAction(player, EmoteAction.YES, 4))
        } else if (slot == 3) {
            player.startAction(EmoteAction(player, EmoteAction.NO, 4))
        } else if (slot == 4) {
            player.startAction(EmoteAction(player, EmoteAction.BOW, 4))
        } else if (slot == 5) {
            player.startAction(EmoteAction(player, EmoteAction.ANGRY, 4))
        } else if (slot == 6) {
            player.startAction(EmoteAction(player, EmoteAction.THINK, 4))
        } else if (slot == 7) {
            player.startAction(EmoteAction(player, EmoteAction.WAVE, 4))
        } else if (slot == 8) {
            player.startAction(EmoteAction(player, EmoteAction.SHRUG, 3))
        } else if (slot == 9) {
            player.startAction(EmoteAction(player, EmoteAction.CHEER, 5))
        } else if (slot == 10) {
            player.startAction(EmoteAction(player, EmoteAction.BECKON, 3))
        } else if (slot == 12) {
            player.startAction(EmoteAction(player, EmoteAction.LAUGH, 4))
        } else if (slot == 11) {
            player.startAction(EmoteAction(player, EmoteAction.JUMP_FOR_JOY, 3))
        } else if (slot == 13) {
            player.startAction(EmoteAction(player, EmoteAction.YAWN, 6))
        } else if (slot == 14) {
            player.startAction(EmoteAction(player, EmoteAction.DANCE, 8))
        } else if (slot == 15) {
            player.startAction(EmoteAction(player, EmoteAction.JIG, 7))
        } else if (slot == 16) {
            player.startAction(EmoteAction(player, EmoteAction.SPIN, 3))
        } else if (slot == 17) {
            player.startAction(EmoteAction(player, EmoteAction.HEADBANG, 7))
        } else if (slot == 18) {
            player.startAction(EmoteAction(player, EmoteAction.CRY, 5))
        } else if (slot == 19) {
            player.startAction(EmoteAction(player, EmoteAction.BLOW_KISS, EmoteAction.BLOW_KISS_GRAPHIC, 11))
        } else if (slot == 20) {
            player.startAction(EmoteAction(player, EmoteAction.PANIC, 4))
        } else if (slot == 21) {
            player.startAction(EmoteAction(player, EmoteAction.RASPBERRY, 5))
        } else if (slot == 22) {
            player.startAction(EmoteAction(player, EmoteAction.CLAP, 6))
        } else if (slot == 23) {
            player.startAction(EmoteAction(player, EmoteAction.SALUTE, 3))
        }
    }
}
