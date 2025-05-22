package net.scapeemulator.game.model

import net.scapeemulator.game.msg.HintIconMessage

abstract class Entity() {

    open lateinit var position: Position

    open val attributes = HashMap<String, Any?>()

    open val hintIcons = arrayOfNulls<HintIconMessage?>(8)
}