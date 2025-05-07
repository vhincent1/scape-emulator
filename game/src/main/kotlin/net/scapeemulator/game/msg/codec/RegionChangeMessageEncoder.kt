package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.msg.RegionChangeMessage
import net.scapeemulator.game.net.game.*
import net.scapeemulator.game.util.LandscapeKeyTable

internal fun regionChangeEncoder(table: LandscapeKeyTable) =
    handleEncoder(RegionChangeMessage::class) { alloc, message ->
        val builder = GameFrameBuilder(alloc, 162, GameFrame.Type.VARIABLE_SHORT)

        val position = message.position
        builder.put(DataType.SHORT, DataTransformation.ADD, position.getLocalX(position.centralRegionX))

        var force = true
        val centralMapX = position.centralRegionX / 8
        val centralMapY = position.centralRegionY / 8

        if ((centralMapX == 48 || centralMapX == 49) && centralMapY == 48) force = false

        if (centralMapX == 48 && centralMapY == 148) force = false

        for (mapX in ((position.centralRegionX - 6) / 8)..((position.centralRegionX + 6) / 8)) {
            for (mapY in ((position.centralRegionY - 6) / 8)..((position.centralRegionY + 6) / 8)) {
                if (force || (mapY != 49 && mapY != 149 && mapY != 147 && mapX != 50 && (mapX != 49 || mapY != 47))) {
                    val keys = table.getKeys(mapX, mapY)
                    for (i in 0..3) builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, keys[i])
                }
            }
        }

        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, position.height)
        builder.put(DataType.SHORT, position.centralRegionX)
        builder.put(DataType.SHORT, DataTransformation.ADD, position.centralRegionY)
        builder.put(DataType.SHORT, DataTransformation.ADD, position.getLocalY(position.centralRegionY))
        return@handleEncoder builder.toGameFrame()
    }

//class RegionChangeMessageEncoder(private val table: LandscapeKeyTable) :
//    MessageEncoder<RegionChangeMessage>(RegionChangeMessage::class) {
//    override fun encode(alloc: ByteBufAllocator, message: RegionChangeMessage): GameFrame {
//        val builder = GameFrameBuilder(alloc, 162, GameFrame.Type.VARIABLE_SHORT)
//
//        val position = message.position
//        builder.put(DataType.SHORT, DataTransformation.ADD, position.getLocalX(position.centralRegionX))
//
//        var force = true
//        val centralMapX = position.centralRegionX / 8
//        val centralMapY = position.centralRegionY / 8
//
//        if ((centralMapX == 48 || centralMapX == 49) && centralMapY == 48) force = false
//
//        if (centralMapX == 48 && centralMapY == 148) force = false
//
//        for (mapX in ((position.centralRegionX - 6) / 8)..((position.centralRegionX + 6) / 8)) {
//            for (mapY in ((position.centralRegionY - 6) / 8)..((position.centralRegionY + 6) / 8)) {
//                if (force || (mapY != 49 && mapY != 149 && mapY != 147 && mapX != 50 && (mapX != 49 || mapY != 47))) {
//                    val keys = table.getKeys(mapX, mapY)
//                    for (i in 0..3) builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, keys[i])
//                }
//            }
//        }
//
//        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, position.height)
//        builder.put(DataType.SHORT, position.centralRegionX)
//        builder.put(DataType.SHORT, DataTransformation.ADD, position.centralRegionY)
//        builder.put(DataType.SHORT, DataTransformation.ADD, position.getLocalY(position.centralRegionY))
//        return builder.toGameFrame()
//    }
//}
