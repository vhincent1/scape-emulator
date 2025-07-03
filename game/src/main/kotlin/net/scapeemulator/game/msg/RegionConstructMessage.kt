package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.model.RegionPalette

data class RegionConstructMessage(val position: Position, val palette: RegionPalette) : Message