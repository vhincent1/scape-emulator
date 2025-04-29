package net.scapeemulator.game.net.login

class LoginRequest(
    val isReconnecting: Boolean,
    val username: String,
    val password: String,
    val clientSessionKey: Long,
    val serverSessionKey: Long,
    val version: Int,
    val crc: IntArray,
    val displayMode: Int
)
