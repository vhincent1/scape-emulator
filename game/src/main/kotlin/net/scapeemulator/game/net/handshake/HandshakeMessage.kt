package net.scapeemulator.game.net.handshake

class HandshakeMessage( val service: Int) {
    companion object {
        const val SERVICE_LOGIN: Int = 14
        const val SERVICE_UPDATE: Int = 15
        const val SERVICE_JAGGRAB: Int = 17
        const val SERVICE_REGISTER_PERSONAL_DETAILS: Int = 147
        const val SERVICE_REGISTER_USERNAME: Int = 186
        const val SERVICE_REGISTER_COMMIT: Int = 36
        const val SERVICE_AUTO_LOGIN: Int = 210
        const val SERVICE_WORLD_LIST: Int = 255
    }
}
