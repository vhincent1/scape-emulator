package net.scapeemulator.game.util.math

import kotlin.math.cos

object Trigonometry {
    val COSINE: IntArray = IntArray(2048)

    init {
        for (i in COSINE.indices) {
            COSINE[i] = (65536 * cos(2 * Math.PI * i / COSINE.size.toDouble())).toInt()
        }
    }
}
