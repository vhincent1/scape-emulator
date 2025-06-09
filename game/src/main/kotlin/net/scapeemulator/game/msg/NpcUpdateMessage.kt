package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.update.NpcDescriptor

data class NpcUpdateMessage(
    val lastKnownRegion: Position,
    @JvmField val position: Position,
    @JvmField val localNpcCount: Int,
    @JvmField val descriptors: List<NpcDescriptor>
) : Message