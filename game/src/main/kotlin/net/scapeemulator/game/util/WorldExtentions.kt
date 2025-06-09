package net.scapeemulator.game.util

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.model.World

fun World.playersWithinScene(position: Position, block: Player.() -> Unit) {
    players.filterNotNull().filter { position.isWithinScene(it.position) }.forEach { block(it) }
}