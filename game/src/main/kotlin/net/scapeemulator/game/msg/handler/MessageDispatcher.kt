package net.scapeemulator.game.msg.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import net.scapeemulator.game.GameServer
import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.command.CommandDispatcher
import net.scapeemulator.game.model.ItemDefinitions
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.msg.GroundItem.ItemDropHandler
import net.scapeemulator.game.msg.codec.ExamineMessage
import net.scapeemulator.game.msg.codec.ExamineType
import kotlin.reflect.KClass

class MessageDispatcher {

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

        //todo clean
        handlers[ObjectOptionOneMessage::class] = MessageHandler<ObjectOptionOneMessage> { player, message ->
            player.sendMessage("obj " + message.id + " x" + message.x)
        }
        handlers[ObjectOptionTwoMessage::class] = MessageHandler<ObjectOptionTwoMessage> { player, message ->
            player.sendMessage("obj " + message.id + " x" + message.x)
        }

        handlers[RunScriptMessage::class] = RunScriptHandler
        handlers[ItemDropMessage::class] = ItemDropHandler
        handlers[GroundItemOptionMessage::class] = MessageHandler<GroundItemOptionMessage> { player, message ->
            val (id, position, option) = message
            player.sendMessage("option=${option} item=$id pos=[${position.x}, ${position.y}]")

            val list = GameServer.INSTANCE.world.groundItemManager.list
            val found = list.find { it?.id == id || it?.position == position }
            found?.apply {
                player.sendMessage("Found: index=$index item=${toItem()} $amount")
            }
            println("LIST:")
            list.filterNotNull().forEach { i -> println("${i.index} ${i.id} ${i.amount}") }
        }

        handlers[ExamineMessage::class] = MessageHandler<ExamineMessage> { player, message ->
            when (message.type) {
                ExamineType.ITEM -> {
                    val item = ItemDefinitions.forId(message.id)
                    if (item != null) player.sendMessage(item.examine)
                }

                ExamineType.NPC -> {}
                ExamineType.OBJECT -> {}
            }
        }
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
