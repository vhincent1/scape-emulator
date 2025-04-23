package net.scapeemulator.game.model

import net.scapeemulator.game.msg.InterfaceOpenMessage
import net.scapeemulator.game.msg.InterfaceRootMessage
import net.scapeemulator.game.msg.ScriptIntMessage
import java.util.*

class InterfaceSet(private val player: Player) {
    enum class DisplayMode {
        FIXED, RESIZABLE
    }

    private val tabs = IntArray(15)
    var fullscreen: Int = -1
        private set
    var displayMode: DisplayMode? = DisplayMode.FIXED

    init {
        Arrays.fill(tabs, -1)
    }

    fun init() {
        // TODO close any windows/overlays/etc. that may be left open if not reconnecting ?
        // also consider the display mode changing
        if (this.displayMode == DisplayMode.FIXED) {
            player.send(InterfaceRootMessage(Interface.FIXED, 0))
            player.send(InterfaceOpenMessage(Interface.FIXED, 75, 752, 1)) // chat box
            player.send(InterfaceOpenMessage(Interface.FIXED, 14, 751, 1)) // chat options
            player.send(InterfaceOpenMessage(752, 8, 137, 1)) // chat username & scroll bar
            player.send(InterfaceOpenMessage(Interface.FIXED, 10, 754, 1)) // PM split chat

            player.send(InterfaceOpenMessage(Interface.FIXED, 70, Interface.HITPOINTS_ORB, 1)) // hitpoints orb
            player.send(InterfaceOpenMessage(Interface.FIXED, 71, Interface.PRAYER_ORB, 1)) // prayer orb
            player.send(InterfaceOpenMessage(Interface.FIXED, 72, Interface.ENERGY_ORB, 1)) // energy orb
            player.send(InterfaceOpenMessage(Interface.FIXED, 73, Interface.SUMMONING_ORB, 1)) // summoning orb
        } else {
            player.send(InterfaceRootMessage(Interface.RESIZABLE, 0))
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 70, 752, 1)) // chat box
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 23, 751, 1)) // chat options
            player.send(InterfaceOpenMessage(752, 8, 137, 1)) // chat username & scroll bar
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 71, 754, 1)) // PM split chat (correct?)

            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 13, Interface.HITPOINTS_ORB, 1)) // hitpoints orb
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 14, Interface.PRAYER_ORB, 1)) // prayer orb
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 15, Interface.ENERGY_ORB, 1)) // energy orb
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 16, Interface.SUMMONING_ORB, 1)) // summoning orb
        }

        Equipment.openAttackTab(player)
        openTab(Tab.SKILLS, Interface.SKILLS)
        openTab(Tab.QUEST, Interface.QUESTS)
        openTab(Tab.INVENTORY, Interface.INVENTORY)
        openTab(Tab.EQUIPMENT, Interface.EQUIPMENT)
        openTab(Tab.PRAYER, Interface.PRAYER)
        openTab(Tab.MAGIC, Interface.MAGIC)
        openTab(Tab.FRIENDS, Interface.FRIENDS)
        openTab(Tab.IGNORES, Interface.IGNORES)
        openTab(Tab.CLAN, Interface.CLAN)
        openTab(Tab.SETTINGS, Interface.SETTINGS)
        openTab(Tab.EMOTES, Interface.EMOTES)
        openTab(Tab.MUSIC, Interface.MUSIC)
        openTab(Tab.LOGOUT, Interface.LOGOUT)

        //openTab(Tab.SUMMONING, Interface.SUMMONING);
        //for (int i = 0; i < 6; i++)
        //	player.send(new InterfaceVisibleMessage(747, i, true));
    }

    fun getTab(tab: Int): Int {
        return tabs[tab]
    }

    fun openTab(tab: Int, id: Int) {
        tabs[tab] = id
        if (this.displayMode == DisplayMode.FIXED) {
            player.send(InterfaceOpenMessage(Interface.FIXED, 83 + tab, id, 1))
        } else {
            // 76 = force a single tab to be shown ?
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 93 + tab, id, 1))
        }
    }

    fun switchToTab(tab: Int) {
        player.send(ScriptIntMessage(168, tab))
    }

    fun closeTab(tab: Int) {
        tabs[tab] = -1
        if (this.displayMode == DisplayMode.FIXED) {
        } else {
        }
    }

    fun openWindow(id: Int) {
        if (this.displayMode == DisplayMode.FIXED) {
            // TODO: another source uses 16?
            player.send(InterfaceOpenMessage(Interface.FIXED, 11, id, 0))
        } else {
            // TODO: id == 499 => slot 5 in xeno
            // TODO: another source: 3 norm, 4 for bank , 6 for help?
            // somewhere else it uses 8?
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 6, id, 0))
        }
    }

    fun openOverlay(id: Int) {
        if (this.displayMode == DisplayMode.FIXED) {
            player.send(InterfaceOpenMessage(Interface.FIXED, 5, id, 1))
        } else {
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 5, id, 1))
        }
    }

    fun openFullscreen(id: Int) {
        fullscreen = id
        player.send(InterfaceRootMessage(id, 0))
    }

    fun closeFullscreen() {
        fullscreen = -1
        if (this.displayMode == DisplayMode.FIXED) {
            player.send(InterfaceRootMessage(Interface.FIXED, 2))
        } else {
            player.send(InterfaceRootMessage(Interface.RESIZABLE, 2))
        }
    }

    fun openWorldMap() {
        openFullscreen(Interface.WORLD_MAP)
        player.send(ScriptIntMessage(622, player.position.toPackedInt())) // map center
        player.send(ScriptIntMessage(674, player.position.toPackedInt())) // player position
    }
}
