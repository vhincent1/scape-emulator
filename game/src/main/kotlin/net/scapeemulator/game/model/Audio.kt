package net.scapeemulator.game.model

import net.scapeemulator.game.msg.Audio

class AudioManager {
    fun send(sound: Audio, players: List<Player>) {}

    fun send(sound: Audio, global: Boolean = false) {
        //regionmanager.getlocalplayers MapDistance.SOUND.getDistance()
    }
}