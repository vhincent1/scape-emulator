package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RegionChangedMessage

private val REGION_CHANGED_MESSAGE = RegionChangedMessage()
internal val regionChangedMessageDecoder = handleDecoder(110) { frame ->
    return@handleDecoder REGION_CHANGED_MESSAGE
}
