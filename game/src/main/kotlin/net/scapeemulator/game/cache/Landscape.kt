package net.scapeemulator.game.cache

import net.scapeemulator.cache.util.ByteBufferUtils
import net.scapeemulator.game.model.GroundObject
import net.scapeemulator.game.model.ObjectType
import net.scapeemulator.game.model.Position
import net.scapeemulator.game.pathfinder.MapListener
import java.nio.ByteBuffer

class Landscape private constructor(val x: Int, val y: Int) {
    //todo add ground object definitions
    val objects = ArrayList<GroundObject>()

    companion object {
        @JvmStatic
        fun decode(listeners: List<MapListener>, list: ArrayList<GroundObject>, x: Int, y: Int, buffer: ByteBuffer): Landscape {
            val landscape = Landscape(x, y)
            var id = -1
            var deltaId: Int
            while ((ByteBufferUtils.getSmart(buffer).also { deltaId = it }) != 0) {
                id += deltaId
                var pos = 0
                var deltaPos: Int
                while ((ByteBufferUtils.getSmart(buffer).also { deltaPos = it }) != 0) {
                    pos += deltaPos - 1
                    val localX = (pos shr 6) and 0x3F
                    val localY = pos and 0x3F
                    val height = (pos shr 12) and 0x3
                    val temp = buffer.get().toInt() and 0xFF
                    val type = (temp shr 2) and 0x3F
                    val rotation = temp and 0x3
                    val position = Position(x * 64 + localX, y * 64 + localY, height)
//                    landscape.objects.add(GroundObject(id, type, position, rotation))
//                    list.add(GroundObject(id, type, position, rotation))
                    listeners.forEach { it.objectDecoded(id, rotation, ObjectType.forId(type), position) }
                }
            }
            return landscape
        }
    }
}
