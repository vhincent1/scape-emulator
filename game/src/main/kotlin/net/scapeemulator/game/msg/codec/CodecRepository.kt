package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.Message
import net.scapeemulator.game.util.LandscapeKeyTable
import kotlin.reflect.KClass

class CodecRepository(table: LandscapeKeyTable) {
    private val inCodecs = arrayOfNulls<MessageDecoder<*>>(256)
    private val outCodecs: MutableMap<KClass<*>, MessageEncoder<*>> = HashMap()

    init {
        /* decoders */
        bind(pingMessageDecoder)
        bind(idleLogoutMessageDecoder)
        bind(buttonMessageDecoder)
        bind(extendedButtonMessageDecoder)


        bind(playerInteraction1)
        bind(playerInteraction2)
        bind(playerInteraction3)
        bind(playerInteraction4)
        bind(playerInteraction5)
        bind(playerInteraction6)
        bind(playerInteraction7)

//        bind(InterfaceOptionMessageDecoder())


        bind(walkMessageDecoder(39))
        bind(walkMessageDecoder(77))
        bind(walkMessageDecoder(215))

        bind(chatMessageDecoder)
        bind(commandMessageDecoder)
        bind(swapItemsMessageDecoder)
        bind(equipItemMessageDecoder)
        bind(displayMessageDecoder)
        bind(removeItemMessageDecoder)
        bind(regionChangedMessageDecoder)
        bind(clickMessageDecoder)
        bind(focusMessageDecoder)
        bind(cameraMessageDecoder)
        bind(flagsMessageDecoder)
        bind(sequenceNumberMessageDecoder)
        bind(interfaceClosedMessageDecoder)//184
        bind(objectOptionOneMessageDecoder)
        bind(objectOptionTwoMessageDecoder)

        /* encoders */
        bind(regionChangeEncoder(table))
        bind(interfaceRootEncoder)
        bind(interfaceOpenEncoder)// 155 sendInterface
        bind(interfaceCloseEncoder)
        bind(interfaceVisibleEncoder)//21, interfaceConfig
        bind(interfaceTextEncoder)
        bind(interfaceItemsMessageEncoder)
        bind(interfaceSlottedItemsEncoder)
        bind(interfaceResetItemsEncoder)
        bind(animateInterfaceEncoder)
        bind(serverMessageEncoder)
        bind(systemUpdateEncoder)
        bind(logoutMessageEncoder)
        bind(playerUpdateMessageEncoder)
        bind(skillMessageEncoder)
        bind(energyMessageEncoder)
        bind(interactionOptionEncoder)
        bind(resetMinimapFlagMessageEncoder)
        bind(configMessageEncoder)
        bind(scriptMessageEncoder)
        bind(npcUpdateMessageEncoder)
        bind(scriptStringMessageEncoder)
        bind(scriptIntMessageEncoder)
        bind(displayModelEncoder)
        bind(clearMinimapFlag)
        bind(hintArrowEncoder)
        bind(minimapStatus)
        bind(playerInteraction)
        bind(enterAmountDecoder)
        bind(enterTextDecoder)
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