package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RegionChangedMessage
import net.scapeemulator.game.net.game.GameFrame
import java.io.IOException

class RegionChangedMessageDecoder : MessageDecoder<RegionChangedMessage>(110) {
    @Throws(IOException::class)
    override fun decode(frame: GameFrame): RegionChangedMessage {
        return REGION_CHANGED_MESSAGE
    }

    companion object {
        private val REGION_CHANGED_MESSAGE = RegionChangedMessage()
    }
}
