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
        handlers[ButtonMessage::class] = buttonMessageHandler(buttonDispatcher)
        handlers[ExtendedButtonMessage::class] = extendedButtonMessageHandler(buttonDispatcher)

        handlers[InteractionMessage::class] = messageHandler<InteractionMessage> { player, message ->
            //todo clear minimap flag
//            println("Interaction: index=${message.option} target=${message.index} type=${message.type}")
        }

        handlers[PingMessage::class] = pingMessageHandler
        handlers[IdleLogoutMessage::class] = idleLogoutMessageHandler
        handlers[WalkMessage::class] = walkMessageHandler
        handlers[ChatMessage::class] = chatMessageHandler

        /* command handler */
        handlers[CommandMessage::class] = commandDispatcher

        handlers[SwapItemsMessage::class] = swapItemsMessageHandler
        handlers[EquipItemMessage::class] = equipItemMessageHandler
        handlers[RemoveItemMessage::class] = removeItemMessageHandler

        handlers[DisplayMessage::class] = displayMessageHandler
        handlers[RegionChangedMessage::class] = regionChangedHandler
        handlers[ClickMessage::class] = clickMessageHandler
        handlers[FocusMessage::class] = focusMessageHandler
        handlers[CameraMessage::class] = cameraMessageHandler
        handlers[FlagsMessage::class] = flagsMessageHandler
        handlers[SequenceNumberMessage::class] = sequenceNumberMessageHandler
        handlers[InterfaceClosedMessage::class] = interfaceClosedMessageHandler
        handlers[ObjectOptionOneMessage::class] = messageHandler<ObjectOptionOneMessage> { player, message ->
            player.sendMessage("obj " + message.id + " x" + message.x)
        }
        handlers[ObjectOptionTwoMessage::class] = messageHandler<ObjectOptionTwoMessage> { player, message ->
            player.sendMessage("obj " + message.id + " x" + message.x)
        }

        handlers[RunScriptMessage::class] = runScriptHandler
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
