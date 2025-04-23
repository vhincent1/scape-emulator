package net.scapeemulator.game.model

import net.scapeemulator.cache.def.ItemDefinition

class Item (id: Int, amount: Int) {
    val id: Int
    val amount: Int

    init {
        require(amount >= 0)
        this.id = id
        this.amount = amount
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + amount
        result = prime * result + id
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        if (javaClass != obj.javaClass) return false
        val other = obj as Item
        if (amount != other.amount) return false
        if (id != other.id) return false
        return true
    }

    override fun toString(): String {
        return Item::class.java.simpleName + " [id=" + id + ", amount=" + amount + "]"
    }

    val definition: ItemDefinition?
        get() = ItemDefinitions.forId(id)

    val equipmentDefinition: EquipmentDefinition?
        get() = EquipmentDefinition.forId(id)
}
