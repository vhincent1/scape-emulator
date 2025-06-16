package net.scapeemulator.game.msg

class WalkMessage(val destination: Step, val steps: Array<Step?>, val isRunning: Boolean) : Message {
    data class Step(val x: Int, val y: Int)
}