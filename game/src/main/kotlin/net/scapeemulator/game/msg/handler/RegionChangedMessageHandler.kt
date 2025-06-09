package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.GameServer
import net.scapeemulator.game.msg.RegionChangedMessage

internal val RegionChangedHandler = MessageHandler<RegionChangedMessage> { player, message ->
    //todo impl
    GameServer.WORLD.groundItemManager.refresh()
}