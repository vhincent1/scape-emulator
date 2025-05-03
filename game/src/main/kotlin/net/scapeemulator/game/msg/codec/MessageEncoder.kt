package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException

abstract class MessageEncoder<T : Message>(@JvmField val clazz: Class<T>) {
    @Throws(IOException::class)
    abstract fun encode(alloc: ByteBufAllocator, message: T): GameFrame
}
