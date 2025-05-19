package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RegionChangedMessage

private val REGION_CHANGED_MESSAGE = RegionChangedMessage()
internal val RegionChangedMessageDecoder = MessageDecoder(110) { frame ->
    return@MessageDecoder REGION_CHANGED_MESSAGE
}
