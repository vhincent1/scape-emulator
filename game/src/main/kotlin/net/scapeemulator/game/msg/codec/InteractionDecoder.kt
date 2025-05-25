package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.InteractionMessage
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
const val PLAYER_OPTION = 68 // attack player
const val PLAYER_OPTION_1 = 106 // player option 1
const val PLAYER_OPTION_2 = 71 // follow player
const val PLAYER_OPTION_3 = 180 // trade player
const val PLAYER_OPTION_4 = 4
const val PLAYER_OPTION_5 = 114
const val PLAYER_OPTION_6 = 175
const val MAGIC_ON_PLAYER = 195 // magic on player

typealias type = InteractionMessage.Type

// NPC action Attack
val NpcInteraction = MessageDecoder(3) { frame ->
    val reader = GameFrameReader(frame)
    val target = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD)
    return@MessageDecoder InteractionMessage(type.NPC, target.toInt(), 0)
}

val PlayerInteraction1 = MessageDecoder(PLAYER_OPTION) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder InteractionMessage(type.PLAYER, index, 0)
}
val PlayerInteraction2 = MessageDecoder(PLAYER_OPTION_1) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT)
    return@MessageDecoder InteractionMessage(type.PLAYER, index.toInt(), 1)
}
val PlayerInteraction3 = MessageDecoder(PLAYER_OPTION_2) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder InteractionMessage(type.PLAYER, index, 2)
}
val PlayerInteraction4 = MessageDecoder(PLAYER_OPTION_3) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder InteractionMessage(type.PLAYER, index, 3)
}
val PlayerInteraction5 = MessageDecoder(PLAYER_OPTION_4) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE).toInt()
    return@MessageDecoder InteractionMessage(type.PLAYER, index, 4)
}
val PlayerInteraction6 = MessageDecoder(PLAYER_OPTION_5) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD).toInt()
    return@MessageDecoder InteractionMessage(type.PLAYER, index, 5)
}
val PlayerInteraction7 = MessageDecoder(PLAYER_OPTION_6) { frame ->
    val reader = GameFrameReader(frame)
    val index = reader.getUnsigned(DataType.SHORT, DataTransformation.ADD).toInt()
    return@MessageDecoder InteractionMessage(type.PLAYER, index, 6)
}