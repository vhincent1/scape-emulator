package net.scapeemulator.game.io

import net.scapeemulator.game.model.Player
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.net.login.LoginResponse

class DummyPlayerSerializer : PlayerSerializer() {
    override fun load(username: String, password: String): SerializeResult {
        val player = Player()
        player.username = username
        player.password = password
        player.rights = 2
        player.position = Position(3222, 3222)
        return SerializeResult(LoginResponse.STATUS_OK, player)
    }

    override fun save(player: Player) {
        /* discard player */
    }
}
