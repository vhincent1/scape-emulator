package net.scapeemulator.game.net.login

class LoginRequest(
    val isReconnecting: Boolean,
    @JvmField val username: String?,
    @JvmField val password: String?,
    val clientSessionKey: Long,
    val serverSessionKey: Long,
    val version: Int,
    val crc: IntArray,
    val displayMode: Int
)
