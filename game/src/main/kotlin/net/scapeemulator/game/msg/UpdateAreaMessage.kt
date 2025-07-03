package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position

data class UpdateAreaMessage(val x: Int, val y: Int) : Message
data class ChunkUpdateMessage(val lastScene: Position, val base: Position): Message