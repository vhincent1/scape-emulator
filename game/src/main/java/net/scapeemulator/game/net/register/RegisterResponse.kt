package net.scapeemulator.game.net.register

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class RegisterResponse {
    /* 38 = cannot create an account at this time */
    val status: Int
    val payload: ByteBuf

    constructor(status: Int) {
        this.status = status
        this.payload = Unpooled.EMPTY_BUFFER
    }

    constructor(status: Int, payload: ByteBuf) {
        this.status = status
        this.payload = payload
    }

    companion object {
        const val STATUS_OK: Int = 2
        const val STATUS_ERROR_CONTACTING_CREATE_SYSTEM: Int = 3
        const val STATUS_SERVER_BUSY: Int = 7
        const val STATUS_CANNOT_CREATE_AT_THIS_TIME: Int = 9
        const val STATUS_DOB_INVALID: Int = 10
        const val STATUS_DOB_FUTURE: Int = 11
        const val STATUS_DOB_THIS_YEAR: Int = 12
        const val STATUS_DOB_LAST_YEAR: Int = 13
        const val STATUS_COUNTRY_INVALID: Int = 14
        const val STATUS_USERNAME_UNAVAILABLE: Int = 20
        const val STATUS_USERNAME_SUGGESTIONS: Int = 21

        /* payload: 1 byte count of usernames, n * 8 byte base37 usernames */
        const val STATUS_USERNAME_INVALID: Int = 22
        const val STATUS_PASSWORD_INVALID_LENGTH: Int = 30
        const val STATUS_PASSWORD_INVALID_CHARS: Int = 31
        const val STATUS_PASSWORD_TOO_EASY: Int = 32

        /* 33 = same as above */
        const val STATUS_PASSWORD_TOO_SIMILAR_TO_USERNAME: Int = 34

        /* 35, 36 = same as above */
        const val STATUS_SERVER_UPDATED: Int = 37
    }
}
