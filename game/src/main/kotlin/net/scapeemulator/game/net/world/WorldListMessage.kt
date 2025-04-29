package net.scapeemulator.game.net.world

class WorldListMessage(
    val sessionId: Int,
    val countries: Array<Country>,
    val worlds: Array<World>,
    val players: IntArray
)
