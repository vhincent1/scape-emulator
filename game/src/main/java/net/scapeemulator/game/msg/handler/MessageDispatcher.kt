package net.scapeemulator.game.msg.handler

import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.log

class MessageDispatcher {

    private val handlers: MutableMap<Class<*>, MessageHandler<*>> = HashMap()
    private val buttonDispatcher = ButtonDispatcher()

    init {
        bind(PingMessage::class.java, PingMessageHandler())
        bind(IdleLogoutMessage::class.java, IdleLogoutMessageHandler())

        bind(ButtonMessage::class.java, ButtonMessageHandler(buttonDispatcher))
        bind(ExtendedButtonMessage::class.java, ExtendedButtonMessageHandler(buttonDispatcher))

        bind(WalkMessage::class.java, WalkMessageHandler())
        bind(ChatMessage::class.java, ChatMessageHandler())
        bind(CommandMessage::class.java, CommandMessageHandler())
        bind(SwapItemsMessage::class.java, SwapItemsMessageHandler())
        bind(EquipItemMessage::class.java, EquipItemMessageHandler())
        bind(DisplayMessage::class.java, DisplayMessageHandler())
        bind(RemoveItemMessage::class.java, RemoveItemMessageHandler())
        bind(RegionChangedMessage::class.java, RegionChangedMessageHandler())
        bind(ClickMessage::class.java, ClickMessageHandler())
        bind(FocusMessage::class.java, FocusMessageHandler())
        bind(CameraMessage::class.java, CameraMessageHandler())
        bind(FlagsMessage::class.java, FlagsMessageHandler())
        bind(SequenceNumberMessage::class.java, SequenceNumberMessageHandler())
        bind(InterfaceClosedMessage::class.java, InterfaceClosedMessageHandler())
        bind(ObjectOptionOneMessage::class.java, object : MessageHandler<ObjectOptionOneMessage>() {
            override fun handle(
                player: Player,
                message: ObjectOptionOneMessage
            ) {
               player.sendMessage("obj "+message.id + " x"+message.x)
            }

        })
    }

    fun <T : Message> bind(clazz: Class<T>, handler: MessageHandler<T>) {
        handlers[clazz] = handler
    }
    
    @Suppress("UNCHECKED_CAST")
    fun dispatch(player: Player, message: Message) {//todo check
        val handler = handlers[message.javaClass] as MessageHandler<Message>?
        if (handler != null) {
            try {
                handler.handle(player, message)
            } catch (t: Throwable) {
                logger.warn("Error processing packet.", t)
            }
        } else {
            logger.warn("Cannot dispatch message (no handler): " + message.javaClass.name + ".")
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MessageDispatcher::class.java)
    }
}
