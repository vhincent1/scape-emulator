package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.cache.LandscapeKeyTable
import net.scapeemulator.game.msg.Message
import kotlin.reflect.KClass

object Opcode {
    const val CHAT_OPCODE: Int = 186
    const val CAMERA: Int = 140
    const val BUTTON: Int = 158
    const val CLICK: Int = 200
    const val COMMAND: Int = 216
    const val DISPLAY_MESSAGE: Int = 86
    const val EQUIPMENT: Int = 55
    const val IDLE_LOGOUT: Int = 245
    const val PING: Int = 137
    const val QUICK_CHAT: Int = 222
    const val WALK: Int = 230
    const val EXAMINE_ITEM: Int = 72
    const val NPC_ATTACK_OPTION: Int = 12
    const val NPC_EXAMINE: Int = 65
    const val WEAR_ITEM: Int = 103
    const val ITEM_ON_ITEM: Int = 58
    const val MOVE_ITEM: Int = 6
    const val REMOVE_ITEM: Int = 81
    const val EXAMINE_OBJECT: Int = 176
    const val EXTENDED_BUTTON: Int = 94
    const val FOCUS_MESSAGE: Int = 248
    const val CLICK_ITEM: Int = 214
    const val OPERATE_ITEM: Int = 124
    const val DROP_ITEM: Int = 88
    const val ITEM_ON_OBJECT: Int = 116
    const val ITEM_ON_PLAYER: Int = 123
    const val CLICK_CHAT_BOX: Int = 105
}

class CodecRepository(table: LandscapeKeyTable) {
    private val inCodecs = arrayOfNulls<MessageDecoder<*>>(256)
    private val outCodecs: MutableMap<KClass<*>, MessageEncoder<*>> = HashMap()

    init {
        /* decoders */
        bind(PingMessageDecoder)
        bind(IdleLogoutMessageDecoder)
        bind(ButtonMessageDecoder)
        bind(ExtendedButtonMessageDecoder)
        bind(PlayerInteraction1)
        bind(PlayerInteraction2)
        bind(PlayerInteraction3)
        bind(PlayerInteraction4)
        bind(PlayerInteraction5)
        bind(PlayerInteraction6)
        bind(PlayerInteraction7)
//        bind(InterfaceOptionMessageDecoder())
        bind(walkMessageDecoder(39))
        bind(walkMessageDecoder(77))
        bind(walkMessageDecoder(215))

        bind(ChatMessageDecoder)
        bind(CommandMessageDecoder)
        bind(SwapItemsMessageDecoder)
        bind(EquipItemMessageDecoder)
        bind(DisplayMessageDecoder)
        bind(RemoveItemMessageDecoder)
        bind(RegionChangedMessageDecoder)
        bind(ClickMessageDecoder)
        bind(FocusMessageDecoder)
        bind(CameraMessageDecoder)
        bind(FlagsMessageDecoder)
        bind(SequenceNumberMessageDecoder)
        bind(InterfaceClosedMessageDecoder)//184
        bind(ObjectOptionOneMessageDecoder)
        bind(ObjectOptionTwoMessageDecoder)
        bind(EnterAmountDecoder)
        bind(EnterTextDecoder)
        bind(NpcInteraction)

        /* encoders */
        bind(RegionChangeEncoder(table))
        bind(RootInterfaceEncoder)
        bind(OpenInterfaceEncoder)// 155 sendInterface
        bind(CloseInterfaceEncoder)
        bind(VisibleInterfaceEncoder)//21, interfaceConfig
        bind(TextInterfaceEncoder)
        bind(InterfaceItemsMessageEncoder)
        bind(InterfaceSlottedItemsEncoder)
        bind(InterfaceResetItemsEncoder)
        bind(AnimateInterfaceEncoder)
        bind(ServerMessageEncoder)
        bind(SystemUpdateEncoder)
        bind(LogoutMessageEncoder)
        bind(PlayerUpdateMessageEncoder)
        bind(SkillMessageEncoder)
        bind(EnergyMessageEncoder)
        bind(InteractionOptionEncoder)
        bind(ResetMinimapFlagMessageEncoder)
        bind(ConfigMessageEncoder)
        bind(ScriptMessageEncoder)
        bind(NpcUpdateMessageEncoder)
        bind(ScriptStringMessageEncoder)
        bind(ScriptIntMessageEncoder)
        bind(DisplayModelEncoder)
        bind(HintArrowEncoder)
        bind(MinimapStatusEncoder)
        bind(WeightEncoder)
        bind(AccessMaskEncoder)
    }

    fun get(opcode: Int): MessageDecoder<*>? = inCodecs[opcode] //TODO: fix
    fun <T : Message> get(clazz: KClass<T>): MessageEncoder<*>? = outCodecs[clazz]
    fun bind(decoder: MessageDecoder<*>) = decoder.also { inCodecs[decoder.opcode] = it }
    fun bind(encoder: MessageEncoder<*>) = encoder.also { outCodecs[encoder.clazz] = it }

    fun bind(encoder: MessageEncoder<*>, opcode: Int) = encoder.also { outCodecs[encoder.clazz] = it }
    fun bind(encoders: Map<KClass<*>, MessageEncoder<*>>) = encoders.forEach { entry -> bind(entry.value) }
}
//    companion object {
//    fun dump() {
//        var count = 0
//        inCodecs.forEach { a ->
//            if (a == null) return@forEach
//            println("opcode=${a.opcode} class=${a.javaClass.name}")
//        }
//        outCodecsDebug.forEach { a ->
//            count++
//            if (a != null)
//                println("opcode=" + count + " class=${a.clazz.qualifiedName}")
//        }
//        println("C" + count)
//    }
//}

//fun main(args: Array<String>) {
//    val codecRepository = CodecRepository(LandscapeKeyTable())
//    codecRepository.dump()
//}