package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.cache.LandscapeKeyTable
import net.scapeemulator.game.msg.Message
import kotlin.reflect.KClass

class CodecRepository(table: LandscapeKeyTable) {
    private val inCodecs = arrayOfNulls<MessageDecoder<*>>(256)
    private val outCodecs: MutableMap<KClass<*>, MessageEncoder<*>> = HashMap()

    init {
        /* decoders */
        bind(PingMessageDecoder)
        bind(IdleLogoutMessageDecoder)
        bind(ButtonMessageDecoder)
        bind(ExtendedButtonMessageDecoder)
//        bind(NpcInteractionDecoder)
//        bind(PlayerInteraction1)
//        bind(PlayerInteraction2)
//        bind(PlayerInteraction3)
//        bind(PlayerInteraction4)
//        bind(PlayerInteraction5)
//        bind(PlayerInteraction6)
//        bind(PlayerInteraction7)
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
        bind(EnterAmountDecoder)
        bind(EnterTextDecoder)

        InteractionDecoders.forEach(this::bind)
        ExamineDecoders.forEach(this::bind)
        ObjectDecoders.forEach(this::bind)

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
        bind(UpdateAreaEncoder)
        bind(SoundEncoder)

        //ground items
        ItemDecoders.forEach(this::bind)
        ItemEncoders.forEach(this::bind)
    }

    fun get(opcode: Int): MessageDecoder<*>? = inCodecs[opcode] //TODO: fix
    fun <T : Message> get(clazz: KClass<T>): MessageEncoder<*>? = outCodecs[clazz]
    private fun bind(decoder: MessageDecoder<*>) = decoder.also { inCodecs[decoder.opcode] = it }
    private fun bind(encoder: MessageEncoder<*>) = encoder.also { outCodecs[encoder.clazz] = it }

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