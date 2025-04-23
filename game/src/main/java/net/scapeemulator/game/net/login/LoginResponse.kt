package net.scapeemulator.game.net.login

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class LoginResponse {
    val status: Int
    val payload: ByteBuf?

    constructor(status: Int) {
        this.status = status
        this.payload = Unpooled.EMPTY_BUFFER
    }

    constructor(status: Int, payload: ByteBuf) {
        this.status = status
        this.payload = payload
    }

    companion object {
        const val STATUS_EXCHANGE_KEYS: Int = 0
        const val STATUS_ADVERTISEMENT: Int = 1
        const val STATUS_OK: Int = 2
        const val STATUS_INVALID_PASSWORD: Int = 3
        const val STATUS_BANNED: Int = 4
        const val STATUS_ALREADY_ONLINE: Int = 5
        const val STATUS_GAME_UPDATED: Int = 6
        const val STATUS_WORLD_FULL: Int = 7
        const val STATUS_LOGIN_SERVER_OFFLINE: Int = 8
        const val STTAUS_LOGIN_LIMIT_EXCEEDED: Int = 9
        const val STATUS_BAD_SESSION_ID: Int = 10
        const val STATUS_FORCE_CHANGE_PASSWORD: Int = 11
        const val STATUS_WORLD_MEMBERS: Int = 12
        const val STATUS_COULD_NOT_COMPLETE: Int = 13
        const val STATUS_UPDATE_IN_PROGRESS: Int = 14
        const val STATUS_TOO_MANY_FAILED_LOGINS: Int = 16
        const val STATUS_IN_MEMBERS_ONLY_AREA: Int = 17
        const val STATUS_ACCOUNT_LOCKED: Int = 18
        const val STATUS_FULLSCREEN_MEMBERS: Int = 19
        const val STATUS_LOGIN_SERVER_INVALID: Int = 20
        const val STATUS_RETRY: Int = 21
        const val STATUS_MALFORMED_PACKET: Int = 22
        const val STATUS_LOGIN_SERVER_NO_REPLY: Int = 23
        const val STATUS_ERROR_LOADING_PROFILE: Int = 24
        const val STATUS_LOGIN_SERVER_INVALID_RESPONSE: Int = 25
        const val STATUS_IP_BANNED: Int = 26
        const val STATUS_SERVICE_UNAVAILABLE: Int = 27
        const val STATUS_CLIENT_MEMBERS: Int = 30
        const val STATUS_SWITCH_WORLD_AND_RETRY: Int = 101
    }
}
