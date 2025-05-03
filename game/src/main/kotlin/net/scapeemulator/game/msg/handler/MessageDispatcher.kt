package net.scapeemulator.game.msg.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.*
import org.jetbrains.kotlin.buildtools.api.KotlinLogger
import kotlin.reflect.KClass

class MessageDispatcher {

    private val handlers: MutableMap<KClass<*>, MessageHandler<*>> = HashMap()
     val buttonDispatcher = ButtonDispatcher()
     val commandDispatcher = CommandMessageHandler()

    init {
        /* action buttons */
        handlers[ButtonMessage::class] = ButtonMessageHandler(buttonDispatcher)
        handlers[ExtendedButtonMessage::class] = ExtendedButtonMessageHandler(buttonDispatcher)

        handlers[PingMessage::class] = PingMessageHandler()
        handlers[IdleLogoutMessage::class] = IdleLogoutMessageHandler()
        handlers[WalkMessage::class] = WalkMessageHandler()
        handlers[ChatMessage::class] = ChatMessageHandler()

        /* command handler */
        handlers[CommandMessage::class] = commandDispatcher

        handlers[SwapItemsMessage::class] = SwapItemsMessageHandler()
        handlers[EquipItemMessage::class] = EquipItemMessageHandler()
        handlers[DisplayMessage::class] = DisplayMessageHandler()
        handlers[RemoveItemMessage::class] = RemoveItemMessageHandler()
        handlers[RegionChangedMessage::class] = RegionChangedMessageHandler()
        handlers[ClickMessage::class] = ClickMessageHandler()
        handlers[FocusMessage::class] = FocusMessageHandler()
        handlers[CameraMessage::class] = CameraMessageHandler()
        handlers[FlagsMessage::class] = FlagsMessageHandler()
        handlers[SequenceNumberMessage::class] = SequenceNumberMessageHandler()
        handlers[InterfaceClosedMessage::class] = InterfaceClosedMessageHandler()
        handlers[ObjectOptionOneMessage::class] = object : MessageHandler<ObjectOptionOneMessage>() {
            override fun handle(
                player: Player,
                message: ObjectOptionOneMessage
            ) {
                player.sendMessage("obj " + message.id + " x" + message.x)
            }

        }
    }

//    fun <T : Message> handlers[clazz: KClass<T>, handler: MessageHandler<T>) {
//        handlers[clazz] = handler
//    }

    @Suppress("UNCHECKED_CAST")
    fun dispatch(player: Player, message: Message) {//todo check
        val handler = handlers[message::class] //as MessageHandler<Message>?
        if (handler != null) {
            try {
                handler.handle(player, message)
            } catch (t: Throwable) {
                logger.warn(t) { "Error processing packet." }
            }
        } else {
            logger.warn { "Cannot dispatch message (no handler): " + message::class.qualifiedName + "." }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger{ }
    }
}
