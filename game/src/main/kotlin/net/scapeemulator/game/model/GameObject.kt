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

class GameObject(var id: Int, val type: Int, position: Position, val rotation: Int) : Entity() {
    init {
        this.position = position
    }
    constructor(id: Int, position: Position) : this(id, 10, position, 0)
    constructor(id: Int, position: Position, rotation: Int) : this(id, 10, position, rotation)
    fun transform(id: Int) = GameObject(id, position, rotation)
    fun transform(id: Int, rotation: Int) = GameObject(id, position, rotation)
    /*doors*/fun transform(id: Int, rotation: Int, position: Position) = GameObject(id, position, rotation)
    val definition: ObjectDefinition? get() = ObjectDefinitions.forId(id)
}

/**
 * Created by Hadyn Richard
 */
enum class ObjectGroup(private val id: Int) {
    /**
     * Enumeration for each group type.
     */
    WALL(0),
    WALL_DECORATION(1),
    GROUP_2(2),
    GROUP_3(3);

    companion object {
        /**
         * The array of object group ids for their type.
         */
        private val OBJECT_GROUPS: IntArray = intArrayOf(
            0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3
        )

        fun forType(type: Int): ObjectGroup {
            val id = OBJECT_GROUPS[type]
            for (group in entries) if (group.id == id) return group
            throw RuntimeException()
        }
    }
}

/**
 * Created by Hadyn Richard
 */
enum class ObjectType(@JvmField val id: Int) {
    /* Each of the types that have been identified */
    DIAGONAL_WALL(9),
    PROP(10),
    DIAGONAL_PROP(11),

    /* Each of the yet to be identified types, some aren't really important */
    STRAIGHT_WALL(0), TYPE_1(1),
    TYPE_2(2), TYPE_3(3),
    TYPE_4(4), TYPE_5(5),
    TYPE_6(6), TYPE_7(7),
    TYPE_8(8), TYPE_12(12),
    TYPE_13(13), TYPE_14(14),
    TYPE_15(15), TYPE_16(16),
    TYPE_17(17), TYPE_18(18),
    TYPE_19(19), TYPE_20(20),
    TYPE_21(21), TYPE_22(22);

    val isWall: Boolean get() = objectGroup == ObjectGroup.WALL
    private val objectGroup: ObjectGroup get() = ObjectGroup.forType(id)

    companion object {
        fun forId(id: Int): ObjectType {
            for (type in entries) if (type.id == id) return type
            throw RuntimeException("unknown object type for id: $id")
        }
    }
}