package net.scapeemulator.game.model

import net.scapeemulator.game.msg.InterfaceTextMessage
import net.scapeemulator.game.msg.InterfaceVisibleMessage

object Equipment {
    const val HEAD: Int = 0
    const val CAPE: Int = 1
    const val NECK: Int = 2
    const val WEAPON: Int = 3
    const val BODY: Int = 4
    const val SHIELD: Int = 5
    const val LEGS: Int = 7
    const val HANDS: Int = 9
    const val FEET: Int = 10
    const val RING: Int = 12
    const val AMMO: Int = 13

    fun remove(player: Player, slot: Int) {
        val inventory = player.inventory
        val equipment = player.equipment

        val item = equipment.get(slot)
        if (item == null) return

        val remaining = inventory.add(item)
        equipment.set(slot, remaining)

        if (slot == WEAPON && remaining == null) {
            weaponChanged(player)
        }
    }

    fun equip(player: Player, slot: Int) {
        val inventory = player.inventory
        val equipment = player.equipment
        val originalWeapon = equipment.get(WEAPON)

        val item = inventory.get(slot)
        if (item == null) return

        val def = item.definition
        if (def == null) return
        val targetSlot = def.equipmentSlot

        val unequipShield = def.equipmentSlot == WEAPON
                && def.isTwoHand
                && equipment.get(SHIELD) != null
        val unequipWeapon =
            targetSlot == SHIELD && equipment.get(WEAPON) != null
                    && equipment.get(WEAPON)!!.definition!!.isTwoHand

        val topUpStack = def.stackable && item.id == equipment.get(targetSlot)?.id

        val drainStack =
            equipment.get(targetSlot) != null
                    && equipment.get(targetSlot)!!.definition!!.stackable
                    && inventory.contains(
                equipment.get(targetSlot)!!.id
            )

        if ((unequipShield || unequipWeapon) && inventory.freeSlots() == 0) {
            inventory.fireCapacityExceeded()
            return
        }

        if (topUpStack) {
            val remaining = equipment.add(item)
            inventory.set(slot, remaining)
        } else {

            if (drainStack) {
                val remaining = inventory.add(equipment.get(targetSlot)!!)
                equipment.set(targetSlot, remaining)
                if (remaining != null) return
            }

            inventory.remove(item, slot)

            val other = equipment.get(targetSlot)
            if (other != null) {
                inventory.add(other)
            }

            equipment.set(targetSlot, item)
        }

        if (unequipShield) {
            val remaining = inventory.add(equipment.get(SHIELD)!!)
            equipment.set(SHIELD, remaining)
        } else if (unequipWeapon) {
            val remaining = inventory.add(equipment.get(WEAPON)!!)
            equipment.set(WEAPON, remaining)
        }

        val weapon = equipment.get(WEAPON)
        var weaponChanged = false
        if (originalWeapon == null && weapon != null)
            weaponChanged = true
        else if (weapon == null && originalWeapon != null)
            weaponChanged = true
        else if (originalWeapon != null && weapon != null && originalWeapon.id != weapon.id)
            weaponChanged = true

        if (weaponChanged) {
            weaponChanged(player)
        }
    }

    private fun weaponChanged(player: Player) {
        // TODO try to keep the same attack style if possible?
        // attackStyle should b named to attackSlot**
        println("WeaponChanged ATK STYLE: " + player.settings.attackStyle)
//        player.settings.attackStyle = 0
        openAttackTab(player)
    }

    fun openAttackTab(player: Player) {
        val weapon = player.equipment.get(WEAPON)
        val def = weapon?.definition

        val name: String
        val weaponClass: WeaponClass
        if (weapon != null && def != null) {
            name = def.name
            weaponClass = def.getWeaponClass()

            //special bar
//            player.settings.refreshSpecialBar()
            player.send(
                InterfaceVisibleMessage(
                    weaponClass.tab,
                    if (weaponClass.attackStyles.size > 3) 12 else 10,
                    def.hasSpecial
                )
            )

        } else {
            name = "Unarmed"
            weaponClass = WeaponClass.UNARMED
        }

        val tab = weaponClass.tab
//        println("WeaponClass: " + weaponClass.name)
//        println("TAB: " + tab)
        player.interfaceSet.openTab(Tab.ATTACK, tab)
        player.send(InterfaceTextMessage(tab, 0, name))

    }
}
