package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Position
import net.scapeemulator.game.update.PlayerDescriptor

data class PlayerUpdateMessage(
    @JvmField val lastKnownRegion: Position,
    @JvmField val position: Position,
    @JvmField val localPlayerCount: Int,
    @JvmField val selfDescriptor: PlayerDescriptor,
    @JvmField val descriptors: List<PlayerDescriptor>
) : Message
