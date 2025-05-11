package net.scapeemulator.game.model

class Appearance(@JvmField val gender: Gender, val style: IntArray, private val colours: IntArray) {

    fun getBody(body: Body) = style[body.ordinal]
    fun getColor(colour: Colour) = colours[colour.ordinal]

    fun setBody(body: Body, value: Int) {
        style[body.ordinal] = value
        println("style[${body.name}] = ${value}")
    }

//    fun setColour(colour: Colour, value: Int) {
//        colours[colour.ordinal] = value
//    }


    companion object {
        @JvmField
        val MALE_APPEARANCE: Appearance = Appearance(
            Gender.MALE,
            intArrayOf(0, 10, 18, 26, 33, 36, 42),
            intArrayOf(2, 5, 8, 11, 14)
        )

        @JvmField
        val FEMALE_APPEARANCE: Appearance = Appearance(
            Gender.FEMALE,
            intArrayOf(45, 1000, 56, 64, 69, 72, 80),
            intArrayOf(2, 5, 8, 11, 14)
        )
    }
}