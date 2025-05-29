/**
 * scape-emulator-final
 * Copyright (c) 2014 Justin Conner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in  the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http:></http:>//www.gnu.org/license/>.
 */
package net.scapeemulator.game.model

import net.scapeemulator.cache.def.ObjectDefinition
import net.scapeemulator.game.cache.ObjectDefinitions

class GameObject(var id: Int, val type: Int, position: Position, rotation: Int) : Entity() {
    val rotation: Int

    init {
        this.position = position
        this.rotation = rotation
    }

    val definition: ObjectDefinition?
        get() = ObjectDefinitions.forId(id) //
    //    @Override
    //    public String toString() {
    //        ObjectDefinition def = getDefinition();
    //
    //        StringBuilder builder = new StringBuilder("GameObject[");
    //        builder.append("id=");
    //        builder.append(id);
    //        builder.append(", type=");
    //        builder.append(type);
    //        builder.append(", width=");
    //        builder.append(def.getWidth());
    //        builder.append(", length=");
    //        builder.append(def.getLength());
    //        builder.append("]");
    //        return builder.toString();
    //    }
}

/**
 * Created by Hadyn Richard
 */
enum class ObjectGroup(
    /**
     * The group id that the type belongs to.
     */
    private val id: Int
) {
    /**
     * Enumation for each group type.
     */
    WALL(0),
    WALL_DECORATION(1),
    GROUP_2(2),
    GROUP_3(3);

    companion object {
        /**
         * The array of object group ids for their type.
         */
        val OBJECT_GROUPS: IntArray = intArrayOf(
            0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 3
        )

        fun forType(type: Int): ObjectGroup {
            val id = OBJECT_GROUPS[type]
            for (group in entries) {
                if (group.id == id) {
                    return group
                }
            }
            throw RuntimeException()
        }
    }
}

object ObjectOrientation {
    const val WEST: Int = 0
    const val NORTH: Int = 1
    const val EAST: Int = 2
    const val SOUTH: Int = 3

    const val ROTATE_CW: Int = 1
    const val ROTATE_CCW: Int = -1

    const val HALF_TURN: Int = 2
}

/**
 * Created by Hadyn Richard
 */
enum class ObjectType(
    /**
     * Gets the type id.
     */
    @JvmField val id: Int
) {
    /* Each of the types that have been identified */
    DIAGONAL_WALL(9),
    PROP(10),
    DIAGONAL_PROP(11),

    /* Each of the yet to be identified types, some arent really important */
    TYPE_0(0), TYPE_1(1),
    TYPE_2(2), TYPE_3(3),
    TYPE_4(4), TYPE_5(5),
    TYPE_6(6), TYPE_7(7),
    TYPE_8(8), TYPE_12(12),
    TYPE_13(13), TYPE_14(14),
    TYPE_15(15), TYPE_16(16),
    TYPE_17(17), TYPE_18(18),
    TYPE_19(19), TYPE_20(20),
    TYPE_21(21), TYPE_22(22);

    val isWall: Boolean
        /**
         * Gets if the object type is apart of the wall group.
         */
        get() = objectGroup == ObjectGroup.WALL

    val objectGroup: ObjectGroup
        /**
         * Gets the object group this type belongs to.
         */
        get() = ObjectGroup.forType(id)

    companion object {
        /**
         * Gets the object type for the specified id.
         */
        fun forId(id: Int): ObjectType {
            for (type in entries) {
                if (type.id == id) {
                    return type
                }
            }
            throw RuntimeException("unknown object type for id: $id")
        }
    }
}