package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.EnergyMessage
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val energyMessageEncoder = handleEncoder(EnergyMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 234)
    builder.put(DataType.BYTE, message.energy)
    return@handleEncoder builder.toGameFrame()
}