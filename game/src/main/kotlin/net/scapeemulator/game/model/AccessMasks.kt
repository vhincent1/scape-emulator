package net.scapeemulator.game.model

import net.scapeemulator.game.msg.AccessMaskMessage

/*
SettingsBuilder settings = new SettingsBuilder();
settings.setSecondaryOption(0, true);
settings.setSecondaryOption(1, true);
settings.setSecondaryOption(2, true);
settings.setSecondaryOption(3, player.getBank().getCustomWithdrawValue() > 0); // this value is for the custom withdraw X, only visible AFTER they have set a custom withdraw value
settings.setSecondaryOption(4, true);
settings.setSecondaryOption(5, true);
settings.setSecondaryOption(6, true);
settings.setSecondaryOption(9, true);
settings.setInterfaceDepth(2);
settings.setCanDragOnto(true);

// 762 = bank child ID
// 0 = starting index/slot
// 516 = ending index/slot
// 93 = component ID
// settings.getValue() is the value from the settings
player.getGamePane().sendAccessMask(762, settings.getValue(), 0, 516, 93); // otherwise known as sendIComponentSettings(childID, settings, start, end, componentID)
 */

/**
 * A class used to configure the bitwise settings for an interface. The settings
 * available include enabling/disabling the primary left click, or right click
 * options, using items/spells/interface components on ground
 * items/npcs/objects/players/yourself/interface components, configuring the
 * interface event height (how high up the hierarchy parents are notified of
 * clicks), and whether the interface components itself can be the target of a
 * 'use with' action
 *
 * @author Mangis
 */
class SettingsBuilder {
    /**
     * Returns the value constructed by this `SettingsBuilder`.
     *
     * [MENTION=181420]Return[/MENTION] the value
     */
    /**
     * Contains the value which should be sent in access mask packet.
     */
    var value: Int = 0
        private set

    /**
     * True if the settings have a left click entityOption
     *
     * [MENTION=181420]Return[/MENTION] True if the settings have a left click entityOption
     */
    fun hasPrimaryOption(): Boolean {
        return (value and 0x1) != 0
    }

    /**
     * True if the settings have the right click entityOption for the given id.
     *
     * [MENTION=141703]Param[/MENTION] optionId the entityOption id, value is 0-9
     * [MENTION=181420]Return[/MENTION] True if the settings have the right click entityOption for the given
     * id.
     */
    fun hasSecondaryOption(optionId: Int): Boolean {
        require(!(optionId < 0 || optionId > 9)) { "Bad entityOption requested: " + optionId }
        return (value and (0x1 shl (optionId + 1))) != 0
    }

    /**
     * True if the settings allow use with items on the ground
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow use with items on the ground
     */
    fun canUseOnGroundItems(): Boolean {
        return (value and (0x1 shl 11)) != 0
    }

    /**
     * True if the settings allow use with npcs
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow use with npcs
     */
    fun canUseOnNPCs(): Boolean {
        return (value and (0x1 shl 12)) != 0
    }

    /**
     * True if the settings allow use with objects
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow use with objects
     */
    fun canUseOnObjects(): Boolean {
        return (value and (0x1 shl 13)) != 0
    }

    /**
     * True if the settings allow use with other players (not necessarily yourself)
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow use with other players (not necessarily
     * yourself)
     */
    fun canUseOnOtherPlayers(): Boolean {
        return (value and (0x1 shl 14)) != 0
    }

    /**
     * True if the settings allow use on themselves
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow use on themselves
     */
    fun canUseOnSelf(): Boolean {
        return (value and (0x1 shl 15)) != 0
    }

    /**
     * True if the settings allow interface components to be dragged
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow interface components to be dragged
     */
    fun canDrag(): Boolean {
        return (value and (0x1 shl 23)) != 0
    }

    /**
     * True if the settings allow items to be dragged onto interface components
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow items to be dragged onto interface
     * components
     */
    fun canDragOnto(): Boolean {
        return (value and (0x1 shl 21)) != 0
    }

    /**
     * True if the settings allow use on other interface components, eg, high
     * alchemy is used on items.
     *
     * [MENTION=181420]Return[/MENTION] True if the settings allow use on other interface components, eg,
     * high alchemy is used on items.
     */
    fun canUseOnInterfaceComponent(): Boolean {
        return (value and (0x1 shl 16)) != 0
    }

    val interfaceDepth: Int
        /**
         * 0-7, the height up the chain to notify parent containers when a button is
         * clicked.
         *
         * [MENTION=181420]Return[/MENTION] 0-7, the height up the chain to notify parent containers when a
         * button is clicked. The higher the height, the further back the
         * parent.
         */
        get() {
            val bits = (value and (0x7 shl 18))
            return bits shr 18
        }

    val isUseOnTarget: Boolean
        /**
         * True if components can be a catalyst in the 'Use With' functionality. For
         * example, items should set this to true when in the inventory to allow for
         * alchemy, while items in the bank should not.
         *
         * [MENTION=181420]Return[/MENTION] true if the components can be a catalyst in teh 'Use With'
         * functionality
         */
        get() = (value and (0x1 shl 22)) != 0

    /**
     * Set's standard entityOption settings. Great example of standard click
     * entityOption is the Continue button in dialog interface. If the entityOption
     * is not allowed the packet won't be send to server.
     *
     * [MENTION=141703]Param[/MENTION] allowed
     */
    fun setPrimaryOption(allowed: Boolean): SettingsBuilder {
        value = value and (0x1).inv()
        if (allowed) value = value or 0x1
        return this
    }

    /**
     * Set's right click entityOption settings. Great example of right click
     * entityOption is the Dismiss entityOption in summoning orb. If specified
     * entityOption is not allowed , it will not appear in right click menu and the
     * packet will not be send to server when clicked.
     */
    fun setSecondaryOption(optionID: Int, allowed: Boolean): SettingsBuilder {
        require(!(optionID < 0 || optionID > 9)) { "optionID must be 0-9." }
        value = value and (0x1 shl (optionID + 1)).inv() // disable
        if (allowed) value = value or (0x1 shl (optionID + 1))
        return this
    }

    /**
     * Sets use on settings. By use on , I mean the options such as Cast in
     * spellbook or use in inventory. If nothing is allowed then 'use' entityOption
     * will not appear in right click menu.
     */
    fun setUseOnSettings(
        canUseOnGroundItems: Boolean,
        canUseOnNpcs: Boolean,
        canUseOnObjects: Boolean,
        canUseOnNonselfPlayers: Boolean,
        canUseOnSelfPlayer: Boolean,
        canUseOnInterfaceComponent: Boolean
    ): SettingsBuilder {
        var useFlag = 0
        if (canUseOnGroundItems) useFlag = useFlag or 0x1
        if (canUseOnNpcs) useFlag = useFlag or 0x2
        if (canUseOnObjects) useFlag = useFlag or 0x4
        if (canUseOnNonselfPlayers) useFlag = useFlag or 0x8
        if (canUseOnSelfPlayer) useFlag = useFlag or 0x10
        if (canUseOnInterfaceComponent) useFlag = useFlag or 0x20
        value = value and (0x7F shl 11).inv() // disable
        value = value or (useFlag shl 11)
        return this
    }

    /**
     * Set's interface events height. For example, we have inventory interface which
     * is opened on gameframe interface (548 or 746). If height is 1 , then the
     * clicks in inventory will also invoke click event handler scripts on gameframe
     * interface.
     */
    fun setInterfaceDepth(depth: Int): SettingsBuilder {
        require(!(depth < 0 || depth > 7)) { "height must be 0-7." }
        value = value and (0x7 shl 18).inv()
        value = value or (depth shl 18)
        return this
    }

    /**
     * Set's canUseOnFlag. if it's true then other interface components can do use
     * on this interface component. Example would be using High alchemy spell on the
     * inventory item. If inventory component where items are stored doesn't allow
     * the canUseOn , it would not be possible to use High Alchemy on that item.
     */
    fun setIsUseOnTarget(allow: Boolean): SettingsBuilder {
        value = value and (1 shl 22).inv()
        if (allow) value = value or (1 shl 22)
        return this
    }

    /**
     * Set's canDragOnto. if it's true items can be dragged onto interface
     * components. An example would be dragging an item in the bank onto a bank tab.
     */
    fun setCanDragOnto(allow: Boolean): SettingsBuilder {
        value = value and (1 shl 21).inv()
        if (allow) value = value or (1 shl 21)
        return this
    }

    /**
     * Set's canUseOnFlag. if it's true, then interface components can be dragged
     */
    fun setCanDrag(allow: Boolean): SettingsBuilder {
        value = value and (1 shl 23).inv()
        if (allow) value = value or (1 shl 23)
        return this
    }
}

/**
 * Contains the mask value methods. The 'access mask' is actually just a bit
 * register that contains permissions for a specific interface (@Peterbjornx)
 * @date 5/02/2013
 * @author Stacx
 */
object BitregisterAssembler {
    /**
     * Holds the first 16 (32 total) values for our bitregister. Each one holds
     * a 'permission' for the interface. (ie. draggable, option) Left out the
     * rest 16 because they're mostly for other things than interface options.
     * {@link http
     * ://www.rune-server.org/runescape-development/rs-503-client-server
     * /informative-threads/184175-about-optionmask-values.html}
     */
    internal val FLAG_CACHE: IntArray = intArrayOf(
        0x2, 0x4, 0x8,                                        /*1*/
        0x10, 0x20, 0x40, 0x80,                              /*2*/
        0x100, 0x200, 0x400, 0x800,                         /*3*/
        0x1000, 0x2000, 0x4000, 0x8000,                    /*4*/
        0x10000, 0x20000, 0x40000, 0x80000,               /*5*/
        0x100000, 0x200000, 0x400000, 0x800000,          /*6*/
        0x1000000, 0x2000000, 0x4000000, 0x8000000,     /*7*/
        0x10000000, 0x20000000, 0x40000000, -0x80000000/*8*/
    )

    /**
     * **Send** and assemble a bit register for our
     * @param player , the player instance
     * @param interfaceIndex , the interface index
     * @param childIndex , the child index for our interface
     * @param offset , the offset for the loop in client
     * @param length , the length of our loop
     */
    fun Player.sendAccessMask(
        interfaceIndex: Int,
        childIndex: Int,
        offset: Int,
        length: Int,
        count: Int
    ) {
        if (offset >= length) throw RuntimeException("Offset cannot excess length. length = $length")
        send(AccessMaskMessage(calculateRegister(count), childIndex, interfaceIndex, offset, length))
    }

    /**
     * Calculates the **bit register**
     * @param count , the amount of options we want to send.
     * @return mask
     */
    fun calculateRegister(count: Int): Int {
        var count = count
        var mask = 0
        while (count != -1) {
            mask = mask or FLAG_CACHE[count--]
        }
        return mask
    } /* todo: identify the flag permissions */
}
