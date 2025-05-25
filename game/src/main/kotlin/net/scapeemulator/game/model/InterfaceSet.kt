package net.scapeemulator.game.model

import net.scapeemulator.game.msg.*
import java.util.*

class InterfaceSet( val player: Player) {
    enum class InterfaceType(
        private val fixedPanel: Int,
        private val resizablePanel: Int,
        private val fixedChild: Int,
        private val resizableChild: Int
    ) {
        DEFAULT(Interface.FIXED, Interface.RESIZABLE, 11, 6),
        OVERLAY(Interface.FIXED, Interface.RESIZABLE, 4, 5),
        TAB(Interface.FIXED, Interface.RESIZABLE, 83, 93),
        SINGLE_TAB(Interface.FIXED, Interface.RESIZABLE, 80, 76),
        DIALOGUE(Interface.CHATBOX, Interface.CHATBOX, 12, 12),
        WINDOW_PANE(Interface.FIXED, Interface.RESIZABLE, 0, 0),
        CLIENTSCRIPT_CHATBOX(Interface.CHATBOX, Interface.CHATBOX, 6, 6),
        CHATBOX(Interface.CHATBOX, Interface.CHATBOX, 8, 8);

        fun getPanel(displayMode: DisplayMode): Int =
            if (displayMode == DisplayMode.FIXED) fixedPanel else resizablePanel

        fun getChild(displayMode: DisplayMode): Int =
            if (displayMode == DisplayMode.FIXED) fixedChild else resizableChild
    }

    enum class DisplayMode {
        FIXED, RESIZABLE
    }

    private val tabs = IntArray(15)
    var fullscreen: Int = -1
        private set
    var displayMode: DisplayMode = DisplayMode.FIXED
    var current: Int = -1

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
            player.send(InterfaceOpenMessage(Interface.CHATBOX, 8, 137, 1)) // chat username & scroll bar
            player.send(InterfaceOpenMessage(Interface.FIXED, 10, 754, 1)) // PM split chat

            player.send(InterfaceOpenMessage(Interface.FIXED, 70, Interface.HITPOINTS_ORB, 1)) // hitpoints orb
            player.send(InterfaceOpenMessage(Interface.FIXED, 71, Interface.PRAYER_ORB, 1)) // prayer orb
            player.send(InterfaceOpenMessage(Interface.FIXED, 72, Interface.ENERGY_ORB, 1)) // energy orb
            player.send(InterfaceOpenMessage(Interface.FIXED, 73, Interface.SUMMONING_ORB, 1)) // summoning orb
        } else {
            player.send(InterfaceRootMessage(Interface.RESIZABLE, 0))
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 70, 752, 1)) // chat box
            player.send(InterfaceOpenMessage(Interface.RESIZABLE, 23, 751, 1)) // chat options
            player.send(InterfaceOpenMessage(Interface.CHATBOX, 8, 137, 1)) // chat username & scroll bar
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
        current = id
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
        current = id
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

    fun close() {
        if (displayMode == DisplayMode.FIXED) {
            player.send(InterfaceCloseMessage(Interface.FIXED, 11))
        } else {
            player.send(InterfaceCloseMessage(Interface.RESIZABLE, 6))
        }
        player.send(ScriptMessage(101, ""))

//        val t = InterfaceType.CHATBOX
//        player.send(InterfaceCloseMessage(t.getPanel(displayMode), t.getChild(displayMode)))
    }

    fun openWorldMap() {
        openFullscreen(Interface.WORLD_MAP)
        player.send(ScriptIntMessage(622, player.position.toPackedInt())) // map center
        player.send(ScriptIntMessage(674, player.position.toPackedInt())) // player position
    }

    //TODO: Lobby screen
//    	public static void sendLobbyScreen(Player player) {
//		Repository.getLobbyPlayers().add(player);
//		player.getPacketDispatch().sendString("Welcome to " + GameWorld.getName() + "", 378, 115);
//		player.getPacketDispatch().sendString(lastLogin(player), 378, 117);
//		final int messages = player.getDetails().getPortal().getMessages();
//		if (messages > 1) {
//			player.getPacketDispatch().sendString("                                                                                                                                                                              " + "You have <col=01DF01>" + messages + " unread message</col> in your message centre.", 378, 15);
//		} else {
//			player.getPacketDispatch().sendString("                                                                                                                                                                              " + "You have " + messages + " unread message in your message centre.", 378, 15);
//		}
//		player.getPacketDispatch().sendString("text1", 378, 39);
//		player.getPacketDispatch().sendString("Visit the forums", 378, 37);
//		player.getPacketDispatch().sendString("Visit the official "+GameWorld.getName()+" forums to stay in touch with the latest updates.", 378, 38);
//		player.getPacketDispatch().sendString("Forums", 378, 14);
//		player.getPacketDispatch().sendString(GameWorld.getName() + " Store", 378, 7);
//		if (player.isDonator()) {
//			player.getPacketDispatch().sendString("text2", 378, 96);
//			player.getPacketDispatch().sendString("You have <col=01DF01>unlimited</col> days of " + GameWorld.getName() + " member credit remaining.", 378, 94);
//			player.getPacketDispatch().sendString("You have an unlimited amount of days of member credit. Thank you for supporting "+GameWorld.getName()+"!", 378, 93);
//		} else {
//			player.getPacketDispatch().sendString("text3", 378, 96);
//			player.getPacketDispatch().sendString("You have zero days of " + GameWorld.getName() + " member credit.", 378, 94);
//			player.getPacketDispatch().sendString("You are not a donator. Choose to donate and you'll get loads of extra benefits and features.", 378, 93);
//		}
//		player.getPacketDispatch().sendString("Never tell anyone your password, even if they claim to work for " + GameWorld.getName() + "!", 378, 56);
//		player.getBankPinManager().drawLoginMessage();
//		//player.getPacketDispatch().sendString(weeklyMessage, WEEKLY_MESSAGE.getComponent(), WEEKLY_MESSAGE.getChild());
//		player.getInterfaceManager().openWindowsPane(new Component(549));
//		player.getInterfaceManager().setOpened(new Component(378));
//		PacketRepository.send(Interface.class, new InterfaceContext(player, 549, 2, 378, true));
//		//PacketRepository.send(Interface.class, new InterfaceContext(player, 549, 3, WEEKLY_MESSAGE.getComponent(), true));
//
//    addChild(new InterfaceChild(3, new MessageOfTheWeek(16, 7,
//    "Spotted a bug? Let the developers know! You can report bugs on the rune-server project thread!")));

//		player.getInterfaceManager().openWindowsPane(new Component(378));
//	}

//    public final class LoginInterfacePlugin extends ComponentPlugin {
//
//        @Override
//        public Plugin<Object> newInstance(Object arg) throws Throwable {
//            ComponentDefinition.put(378, this);
//            return null;
//        }
//
//        @Override
//        public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
//            if (player.getLocks().isLocked("login")) {
//                return true;
//            }
//            player.getLocks().lock("login", 2);
//            player.getPulseManager().run(new Pulse(1) {
//                @Override
//                public boolean pulse() {
//                    LoginConfiguration.configureGameWorld(player);
//                    return true;
//                }
//            });
//            return true;
//        }
//
//    }
    //    var lastLogin
//    private fun lastLogin(): String {
//        var lastLogin
//        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
//        val time: Long = lastLogin
//        var diffDays: Long = -1
//        if (time != -1L) {
//            val currentTime = dateFormat.getCalendar().getTime().time
//            diffDays = (currentTime - time) / (24 * 60 * 60 * 1000)
//        }
//        lastLogin = dateFormat.getCalendar().getTime().time
//        if (diffDays < 0) {
//            return "Welcome to !"
//        }
//        if (diffDays == 0L) {
//            return "You last logged in <col=ff0000>earlier today."
//        }
//        if (diffDays == 1L) {
//            return "You last logged in <col=ff0000> yesterday."
//        }
//        if (diffDays >= 2) {
//            return "You last logged in <col=ff0000> " + diffDays + " days ago." // <col=000000>
//            // from:
//            // "+player.getDetails().getIp() + "
//        }
//        return null
//    }

}
