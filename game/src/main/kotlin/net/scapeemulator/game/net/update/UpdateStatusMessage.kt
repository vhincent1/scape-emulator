package net.scapeemulator.game.net.update

class UpdateStatusMessage( val status: Int) {
    companion object {
        const val STATUS_OK: Int = 0
        const val STATUS_OUT_OF_DATE: Int = 6
        const val STATUS_SERVER_FULL: Int = 7
    }
}
