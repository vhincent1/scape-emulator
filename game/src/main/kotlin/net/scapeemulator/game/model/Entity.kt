package net.scapeemulator.game.model

import net.scapeemulator.game.msg.HintIconMessage

abstract class Entity {
    companion object {
        const val MAX_HINT_ICONS = 8
    }

    open lateinit var position: Position
    val attributes = HashMap<String, Any>()
    open val hintIcons = arrayOfNulls<HintIconMessage?>(MAX_HINT_ICONS)

    init {
        /* attributes */
//        attributes.apply {
//            put("hintIcons", arrayOfNulls<HintIconMessage?>(8))
//        }
    }

}