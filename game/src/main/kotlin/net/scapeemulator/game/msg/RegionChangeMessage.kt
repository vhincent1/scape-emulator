package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position

data class RegionChangeMessage(val position: Position) : Message()
