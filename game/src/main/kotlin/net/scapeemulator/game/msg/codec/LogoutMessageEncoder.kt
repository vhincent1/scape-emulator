package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.LogoutMessage
import net.scapeemulator.game.net.game.GameFrameBuilder

internal val LogoutMessageEncoder = handleEncoder(LogoutMessage::class) { alloc, message ->
    val builder = GameFrameBuilder(alloc, 86)
    return@handleEncoder builder.toGameFrame()
}