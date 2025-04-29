package net.scapeemulator.game.model

class Appearance(val gender: Gender, val style: IntArray, val colors: IntArray) {
//    fun getStyle(index: Int): Int {
//        return style[index]
//    }
//
//    fun getColor(index: Int): Int {
//        return colors[index]
//    }

    companion object {
        @JvmField
        val DEFAULT_APPEARANCE: Appearance = Appearance(
            Gender.MALE,
            intArrayOf(0, 10, 18, 26, 33, 36, 42),
            intArrayOf(2, 5, 8, 11, 14)
        )
    }
}
