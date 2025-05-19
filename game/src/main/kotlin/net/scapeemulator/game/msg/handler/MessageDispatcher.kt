package net.scapeemulator.game.msg.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.command.CommandDispatcher
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.*
import kotlin.reflect.KClass

class MessageDispatcher() {

    private val handlers: MutableMap<KClass<*>, MessageHandler<*>> = HashMap()

    val buttonDispatcher = ButtonDispatcher()
    val commandDispatcher = CommandDispatcher()

    init {
        /* action buttons */
        handlers[ButtonMessage::class] = ButtonMessageHandler(buttonDispatcher)
        handlers[ExtendedButtonMessage::class] = ExtendedButtonMessageHandler(buttonDispatcher)

        handlers[InteractionMessage::class] = MessageHandler<InteractionMessage> { player, message ->
            //todo clear minimap flag
//            println("Interaction: index=${message.option} target=${message.index} type=${message.type}")
        }

        handlers[PingMessage::class] = PingMessageHandler
        handlers[IdleLogoutMessage::class] = IdleLogoutMessageHandler
        handlers[WalkMessage::class] = WalkMessageHandler
        handlers[ChatMessage::class] = ChatMessageHandler

        /* command handler */
        handlers[CommandMessage::class] = commandDispatcher

        handlers[SwapItemsMessage::class] = SwapItemsMessageHandler
        handlers[EquipItemMessage::class] = EquipItemMessageHandler
        handlers[RemoveItemMessage::class] = RemoveItemMessageHandler
        handlers[DisplayMessage::class] = DisplayMessageHandler
        handlers[RegionChangedMessage::class] = RegionChangedHandler
        handlers[ClickMessage::class] = ClickMessageHandler
        handlers[FocusMessage::class] = FocusMessageHandler
        handlers[CameraMessage::class] = CameraMessageHandler
        handlers[FlagsMessage::class] = FlagsMessageHandler
        handlers[SequenceNumberMessage::class] = SequenceNumberMessageHandler
        handlers[InterfaceClosedMessage::class] = InterfaceClosedMessageHandler
        handlers[ObjectOptionOneMessage::class] = MessageHandler<ObjectOptionOneMessage> { player, message ->
            player.sendMessage("obj " + message.id + " x" + message.x)
        }
        handlers[ObjectOptionTwoMessage::class] = MessageHandler<ObjectOptionTwoMessage> { player, message ->
            player.sendMessage("obj " + message.id + " x" + message.x)
        }

        handlers[RunScriptMessage::class] = RunScriptHandler
    }

//    fun <T : Message> handlers[clazz: KClass<T>, handler: MessageHandler<T>) {
//        handlers[clazz] = handler
//    }

    @Suppress("UNCHECKED_CAST")
    internal fun dispatch(player: Player, message: Message) {//todo check
        val handler = handlers[message::class] //as MessageHandler<Message>?
        if (handler != null) {
            try {
                handler.handle(player, message)
//                plugins.notify(MessageEvent(player, message))
            } catch (t: Throwable) {
                logger.warn(t) { "Error processing packet." }
            }
        } else {
            logger.warn { "Cannot dispatch message (no handler): " + message::class.qualifiedName + "." }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
