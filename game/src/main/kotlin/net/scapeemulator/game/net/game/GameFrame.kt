package net.scapeemulator.game.net.game

import io.netty.buffer.ByteBuf

class GameFrame(opcode: Int, type: Type, payload: ByteBuf) {
    enum class Type {
        RAW, FIXED, VARIABLE_BYTE, VARIABLE_SHORT
    }

    @JvmField
    val opcode: Int

    @JvmField
    val type: Type

    @JvmField
    val payload: ByteBuf

    init {
        require(type != Type.RAW)

        this.opcode = opcode
        this.type = type
        this.payload = payload
    }
}
