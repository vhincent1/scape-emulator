package net.scapeemulator.game.model

/**
 * A.K.A. access mask builder.
 * @author Mangis
 */
class InterfaceComponentSettingsBuilder {
    /**
     * Contains the value which should be sended in access mask packet.
     */
    var value: Int = 0
        private set


    /**
     * Set's standart option settings.
     * Great example of standart click option is the Continue button in dialog interface.
     * If the option is not allowed the packet won't be send to server.
     * @param allowed
     */
    fun setStandartClickOptionSettings(allowed: Boolean) {
        value = value and (0x1).inv()
        if (allowed) value = value or 0x1
    }


    /**
     * Set's right click option settings.
     * Great example of right click option is the Dismiss option in summoning orb.
     * If specified option is not allowed , it will not appear in right click menu and the packet will not be send
     * to server when clicked.
     */
    fun setRightClickOptionSettings(optionID: Int, allowed: Boolean) {
        require(!(optionID < 0 || optionID > 9)) { "optionID must be 0-9." }
        value = value and (0x1 shl (optionID + 1)).inv() // disable
        if (allowed) value = value or (0x1 shl (optionID + 1))
    }


    /**
     * Sets use on settings.
     * By use on , I mean the options such as Cast in spellbook or use in inventory.
     * If nothing is allowed then 'use' option will not appear in right click menu.
     */
    fun setUseOnSettings(
        canUseOnGroundItems: Boolean,
        canUseOnNpcs: Boolean,
        canUseOnObjects: Boolean,
        canUseOnNonselfPlayers: Boolean,
        canUseOnSelfPlayer: Boolean,
        canUseOnInterfaceComponent: Boolean
    ) {
        var useFlag = 0
        if (canUseOnGroundItems) useFlag = useFlag or 0x1
        if (canUseOnNpcs) useFlag = useFlag or 0x2
        if (canUseOnObjects) useFlag = useFlag or 0x4
        if (canUseOnNonselfPlayers) useFlag = useFlag or 0x8
        if (canUseOnSelfPlayer) useFlag = useFlag or 0x10
        if (canUseOnInterfaceComponent) useFlag = useFlag or 0x20
        value = value and (127 shl 11).inv() // disable
        value = value or (useFlag shl 11)
    }

    /**
     * Set's interface events depth.
     * For example, we have inventory interface which is opened
     * on gameframe interface (548 or 746).
     * If depth is 1 , then the clicks in inventory will also invoke click event handler scripts
     * on gameframe interface.
     */
    fun setInterfaceEventsDepth(depth: Int) {
        require(!(depth < 0 || depth > 7)) { "depth must be 0-7." }
        value = value and (0x7 shl 18).inv()
        value = value or (depth shl 18)
    }

    /**
     * Set's canUseOnFlag.
     * if it's true then other interface components can do use on this interface component.
     * Example would be using High alchemy spell on the inventory item.
     * If inventory component where items are stored doesn't allow the canUseOn , it would not be possible to use High Alchemy on that
     * item.
     */
    fun setCanUseOn(allow: Boolean) {
        value = value and (1 shl 22).inv()
        if (allow) value = value or (1 shl 22)
    }
}