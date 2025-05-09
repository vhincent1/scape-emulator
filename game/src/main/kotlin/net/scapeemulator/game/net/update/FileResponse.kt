package net.scapeemulator.game.net.update

import io.netty.buffer.ByteBuf

class FileResponse(
    val isPriority: Boolean,
    @JvmField val type: Int,
    @JvmField val file: Int,
    @JvmField val container: ByteBuf?
)
