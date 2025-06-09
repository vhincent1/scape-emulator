package net.scapeemulator.game.msg

class WalkMessage(
    val destination: Step,
    val steps: Array<Step?>,
    val isRunning: Boolean,
    val minimapFlag: Int
) : Message {
    data class Step(val x: Int, val y: Int)
}