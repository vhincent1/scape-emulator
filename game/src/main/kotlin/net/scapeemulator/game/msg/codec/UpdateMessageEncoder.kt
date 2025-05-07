package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.NpcUpdateMessage
import net.scapeemulator.game.msg.PlayerUpdateMessage
import net.scapeemulator.game.net.game.GameFrame
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val playerUpdateMessageEncoder = handleEncoder(PlayerUpdateMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 225, GameFrame.Type.VARIABLE_SHORT)
    val blockBuilder = GameFrameBuilder(alloc)
    builder.switchToBitAccess()

    message.selfDescriptor.encode(message, builder, blockBuilder)
    builder.putBits(8, message.localPlayerCount)

    for (descriptor in message.descriptors) descriptor.encode(message, builder, blockBuilder)

    if (blockBuilder.length > 0) {
        builder.putBits(11, 2047)
        builder.switchToByteAccess()
        builder.putRawBuilder(blockBuilder)
    } else {
        builder.switchToByteAccess()
    }

    return@handleEncoder builder.toGameFrame()
}

internal val npcUpdateMessageEncoder = handleEncoder(NpcUpdateMessage::class) { alloc, message ->

    val builder = GameFrameBuilder(alloc, 32, GameFrame.Type.VARIABLE_SHORT)
    val blockBuilder = GameFrameBuilder(alloc)
    builder.switchToBitAccess()

    builder.putBits(8, message.localNpcCount)

    for (descriptor in message.descriptors) descriptor.encode(message, builder, blockBuilder)

    if (blockBuilder.length > 0) {
        builder.putBits(15, 32767)
        builder.switchToByteAccess()
        builder.putRawBuilder(blockBuilder)
    } else {
        builder.switchToByteAccess()
    }

    return@handleEncoder builder.toGameFrame()
}