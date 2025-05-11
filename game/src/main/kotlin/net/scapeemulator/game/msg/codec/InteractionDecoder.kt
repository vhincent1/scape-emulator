package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.PlayerInteractionMessage
import net.scapeemulator.game.net.game.DataOrder
import net.scapeemulator.game.net.game.DataTransformation
import net.scapeemulator.game.net.game.DataType
import net.scapeemulator.game.net.game.GameFrameReader

//68
//event.player.send(InteractionOptionMessage(0, "Attack")) //68
//event.player.send(InteractionOptionMessage(1, "Trade with")) //106
//event.player.send(InteractionOptionMessage(2, "2")) //71
//event.player.send(InteractionOptionMessage(3, "Test")) //180
//event.player.send(InteractionOptionMessage(4, "Hi")) //4
//event.player.send(InteractionOptionMessage(6, "Request Assistance")) // 114 - req assist
//event.player.send(InteractionOptionMessage(7, "Hi")) //175 whack/control
const val PLAYER_OPTION_1 = 68 // attack player
const val PLAYER_OPTION_2 = 106 // player option 2
const val PLAYER_OPTION_3 = 71 // follow player
const val PLAYER_OPTION_4 = 180 // trade player
const val PLAYER_OPTION_5 = 4
const val PLAYER_OPTION_6 = 114
const val PLAYER_OPTION_7 = 175
const val MAGIC_ON_PLAYER = 195 // magic on player

val playerInteraction = handleDecoder(3) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD)
    println("Opcode: $index")
    return@handleDecoder PlayerInteractionMessage(index.toInt(), 69)
}

val playerInteraction1 = handleDecoder(PLAYER_OPTION_1) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD)
    return@handleDecoder PlayerInteractionMessage(index.toInt(), 1)
}
//todo
val playerInteraction2 = handleDecoder(PLAYER_OPTION_2) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT)
    return@handleDecoder PlayerInteractionMessage(index.toInt(), 2)
}
val playerInteraction3 = handleDecoder(PLAYER_OPTION_3) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD)
    return@handleDecoder PlayerInteractionMessage(index.toInt(), 3)
}
val playerInteraction4 = handleDecoder(PLAYER_OPTION_4) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD)
    return@handleDecoder PlayerInteractionMessage(index.toInt(), 4)
}

val playerInteraction5 = handleDecoder(PLAYER_OPTION_5) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE)
    return@handleDecoder PlayerInteractionMessage(index.toInt(),  5)
}

val playerInteraction6 = handleDecoder(PLAYER_OPTION_6) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD)
    return@handleDecoder PlayerInteractionMessage(index.toInt(), 6)
}

val playerInteraction7 = handleDecoder(PLAYER_OPTION_7) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT,  DataTransformation.ADD)
    return@handleDecoder PlayerInteractionMessage(index.toInt(), 7)
}



