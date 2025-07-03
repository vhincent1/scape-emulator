package net.scapeemulator.game.msg.codec

import net.scapeemulator.game.cache.LandscapeKeyTable
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.msg.RegionConstructMessage
import net.scapeemulator.game.net.game.*

internal fun RegionConstructEncoder(keyTable: LandscapeKeyTable) =
    MessageEncoder(RegionConstructMessage::class) { alloc, message ->
        val builder = GameFrameBuilder(alloc, 214, GameFrame.Type.VARIABLE_SHORT)
        val position: Position = message.position
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.getLocalX())
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.centralRegionX)
        builder.put(DataType.BYTE, DataTransformation.SUBTRACT, position.height % 4)
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.getLocalY())
        builder.switchToBitAccess()
        for (height in 0..3) {
            for (x in 0..12) {
                for (y in 0..12) {
                    val hash: Int = message.palette.getHash(height, x, y)
                    if (hash != -1) {
                        builder.putBit(1)
                        builder.putBits(26, hash)
                    } else builder.putBit(0)
                }
            }
        }
        builder.switchToByteAccess()
        val sentKeys: MutableSet<Int> = HashSet()
        for (height in 0..3) {
            for (x in 0..12) {
                for (y in 0..12) {
                    val hash: Int = message.palette.getHash(height, x, y)
                    if (hash == -1) continue
                    val hashX = (hash shr 14 and 1023) / 8
                    val hashY = (hash shr 3 and 1023) / 8
                    val region = hashY + (hashX shl 8)
                    if (!sentKeys.add(region)) continue
                    val keys: IntArray = keyTable.getKeys(hashX, hashY)
                    for (i in 0..3) builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, keys[i])
                }
            }
        }
        builder.put(DataType.SHORT, position.centralRegionY)
        return@MessageEncoder builder.toGameFrame()
    }