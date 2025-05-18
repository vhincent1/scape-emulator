package net.scapeemulator.game.msg

import net.scapeemulator.game.model.Entity
import net.scapeemulator.game.model.Npc
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Position

class HintIconMessage(
    var slot: Int, val targetType: Int, val targetId: Int, val entity: Entity?,
) : Message() {
    val arrowId = 1 // -1 remove 0 full 1 hollow
    var modelId = 65535
    var position: Position? = null
    var remove: Boolean = false

    fun regis(player: Player) {
        player.send(
            HintIconMessage(
                slot = 1,
                targetType = 10, //1 npc 10 player 2 ground 0 none

                targetId = 1,//idx
                entity = player,
            )
        )
    }

    // arrow on position
    constructor(slot: Int, position: Position) : this(
        slot = slot,
        targetType = 2,
        targetId = -1,
        entity = null
    ) {
        this.position = position
    }

    // arrow on target
    constructor(slot: Int, target: Int, entity: Entity) : this(
        slot = slot,
        targetType = if (entity is Player) 10 else if (entity is Npc) 1 else 2,
        targetId = target,//entity.index,
        entity = entity,
    )

    // arrow on entity (remove)
    constructor(slot: Int, entity: Entity) : this(
        slot = slot,
        targetType = if (entity is Player) 10 else if (entity is Npc) 1 else 2,
        targetId = -1,//entity.index,
        entity = entity,
    )
}