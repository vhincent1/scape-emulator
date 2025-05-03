package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.util.LandscapeKeyTable

class CodecRepository(table: LandscapeKeyTable) {
    private val inCodecs = arrayOfNulls<MessageDecoder<*>>(256)
    private val outCodecs: MutableMap<Class<*>, MessageEncoder<*>> = HashMap()

    init {
        /* decoders */
        bind(PingMessageDecoder())
        bind(IdleLogoutMessageDecoder())

        bind(ButtonMessageDecoder())
        bind(ExtendedButtonMessageDecoder())

//        bind(InterfaceOptionMessageDecoder())

        bind(WalkMessageDecoder(39))
        bind(WalkMessageDecoder(77))
        bind(WalkMessageDecoder(215))
        bind(ChatMessageDecoder())
        bind(CommandMessageDecoder())
        bind(SwapItemsMessageDecoder())
        bind(EquipItemMessageDecoder())
        bind(DisplayMessageDecoder())
        bind(RemoveItemMessageDecoder())
        bind(RegionChangedMessageDecoder())
        bind(ClickMessageDecoder())
        bind(FocusMessageDecoder())
        bind(CameraMessageDecoder())
        bind(FlagsMessageDecoder())
        bind(SequenceNumberMessageDecoder())
        bind(InterfaceClosedMessageDecoder())//184
        bind(ObjectOptionOneMessageDecoder())
        bind(ObjectOptionTwoMessageDecoder())

        /* encoders */
        bind(RegionChangeMessageEncoder(table))
        bind(InterfaceRootMessageEncoder())
        bind(InterfaceOpenMessageEncoder())// 155 sendInterface
        bind(InterfaceCloseMessageEncoder())
        bind(InterfaceVisibleMessageEncoder()) //21, interfaceConfig
        bind(InterfaceTextMessageEncoder())
        bind(ServerMessageEncoder())
        bind(LogoutMessageEncoder())
        bind(PlayerUpdateMessageEncoder())
        bind(SkillMessageEncoder())
        bind(EnergyMessageEncoder())
        bind(InterfaceItemsMessageEncoder())

        bind(InteractionOptionEncoder())

        bind(InterfaceSlottedItemsMessageEncoder())
        bind(InterfaceResetItemsMessageEncoder())
        bind(ResetMinimapFlagMessageEncoder())
        bind(ConfigMessageEncoder())
        bind(ScriptMessageEncoder())
        bind(NpcUpdateMessageEncoder())
        bind(ScriptStringMessageEncoder())
        bind(ScriptIntMessageEncoder())
    }

    fun get(opcode: Int): MessageDecoder<*>? = inCodecs[opcode] //TODO: fix
    fun <T : Message> get(clazz: Class<T>): MessageEncoder<*>? = outCodecs[clazz]
    fun bind(decoder: MessageDecoder<*>) = decoder.also { inCodecs[decoder.opcode] = it }
    fun bind(encoder: MessageEncoder<*>) = encoder.also { outCodecs[encoder.clazz] = it }

}
