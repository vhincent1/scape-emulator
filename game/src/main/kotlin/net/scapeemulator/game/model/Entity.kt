package net.scapeemulator.game.model

import net.scapeemulator.game.msg.HintIconMessage

abstract class Entity {
    open val size = 0
    open lateinit var position: Position
    open val attributes = HashMap<String, Any?>()
    open val hintIcons = arrayOfNulls<HintIconMessage?>(8)
}

abstract class Node {
    open var index = 0
    open lateinit var position: Position

}