package net.scapeemulator.game.net.register

import java.util.*

class RegisterCommitRequest(
    val version: Int,
    val username: String,
    val password: String,
    val dateOfBirth: Calendar,
    val country: Int
)
