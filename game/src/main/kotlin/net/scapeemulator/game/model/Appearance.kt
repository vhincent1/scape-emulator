package net.scapeemulator.game.model

class Appearance(@JvmField val gender: Gender, val style: IntArray, val colours: IntArray) {

    fun getBody(body: Body) = style[body.ordinal]
    fun getColor(colour: Colour) = colours[colour.ordinal]

    fun setBody(body: Body, value: Int) {
        style[body.ordinal] = value
        println("style[${body.name}] = $value")
    }

    constructor(gender: Gender) : this(
        if (gender == Gender.MALE) Gender.MALE else Gender.FEMALE,
        if (gender == Gender.MALE) intArrayOf(0, 10, 18, 26, 33, 36, 42) else intArrayOf(45, 1000, 56, 64, 69, 72, 80),
        if (gender == Gender.MALE) intArrayOf(2, 5, 8, 11, 14) else intArrayOf(2, 5, 8, 11, 14)
    )

//    fun setColour(colour: Colour, value: Int) {
//        colours[colour.ordinal] = value
//    }

}