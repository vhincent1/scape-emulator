package net.scapeemulator.game.msg.codec

import io.netty.buffer.ByteBufAllocator
import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException
import kotlin.reflect.KClass

abstract class MessageEncoder<T : Message>(@JvmField val clazz: KClass<T>) {
    @Throws(IOException::class)
    abstract fun encode(alloc: ByteBufAllocator, message: T): GameFrame
}

fun <T : Message> handleEncoder(
    klass: KClass<T>,
    block: (alloc: ByteBufAllocator, message: T) -> GameFrame
): MessageEncoder<T> {
    return object : MessageEncoder<T>(klass) {
        override fun encode(alloc: ByteBufAllocator, message: T): GameFrame {
            return block(alloc, message)
        }
    }
}