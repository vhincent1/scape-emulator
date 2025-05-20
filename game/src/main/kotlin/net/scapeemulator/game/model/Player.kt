package net.scapeemulator.game.model

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.net.game.GameSession

//todo Player(world, account, session)
class Player() : Mob() {
    var worldId = 0
    var databaseId: Int = 0
    var session: GameSession? = null

    lateinit var username: String
    lateinit var password: String
    var rights: Int = 0

    val localPlayers: MutableList<Player> = ArrayList<Player>()
    val localNpcs: MutableList<Npc> = ArrayList()

    val inventory: Inventory = Inventory(28)
    val equipment: Inventory = Inventory(14)
    val bank: Inventory = Inventory(496, Inventory.StackMode.ALWAYS)
    var chatMessage: ChatMessage? = null
    val settings: PlayerSettings = PlayerSettings(this)
    val interfaceSet: InterfaceSet = InterfaceSet(this)

    var runScript: RunScript? = null

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

//    var nextAttack =-1

    val appearanceTickets: IntArray = IntArray(World.MAX_PLAYERS)
    var appearanceTicketCounter = 0
    var appearanceTicket: Int = nextAppearanceTicket()
        private set

    var appearance: Appearance = Appearance(Gender.FEMALE)
        set(appearance) {
            field = appearance
            this.appearanceTicket = nextAppearanceTicket()
        }

    var energy: Int = 100
        set(energy) {
            field = energy
            this.send(EnergyMessage(energy))
        }

    val isChatUpdated: Boolean get() = chatMessage != null
    var isRegionChanging: Boolean = false
    var lastKnownRegion: Position? = null
        set(lastKnownRegion) {
            field = lastKnownRegion
            this.isRegionChanging = true
        }

    val stance: Int
        get() {
            val weapon = equipment.get(Equipment.WEAPON)

//            val def = weapon.definition
            return weapon?.definition?.getStance() ?: 1426
        }

    init {
        init()
    }

    override fun login() {
        //online = true
        /* set up player for their initial region change */
        val position = position
        lastKnownRegion = position
        send(RegionChangeMessage(position))

        /* set up the game interface */
        interfaceSet.init()
        sendMessage("Welcome to HustlaScape. q p")
        sendMessage("                                                 W")

        /* refresh skills, inventory, energy, etc. */
        inventory.refresh()
        bank.refresh()
        equipment.refresh()
        settings.refresh()
        skillSet.refresh()
        energy = energy // TODO: nicer way than this?
    }

    private fun init() {
        skillSet.addListener(SkillMessageListener(this))
        skillSet.addListener(SkillAppearanceListener(this))

        inventory.addListener(InventoryMessageListener(this, 149, 0, 93))
        inventory.addListener(InventoryFullListener(this, "inventory"))

        bank.addListener(InventoryFullListener(this, "bank"))

        equipment.addListener(InventoryMessageListener(this, 387, 28, 94))
        equipment.addListener(InventoryFullListener(this, "equipment"))
        equipment.addListener(InventoryAppearanceListener(this))
    }

    fun send(message: Message): ChannelFuture? {
        return session?.send(message)
    }

    fun sendMessage(text: String) {
        send(ServerMessage(text))
    }

    override fun logout() {
        // TODO this seems fragile
        val future = send(LogoutMessage())
        future?.addListener(ChannelFutureListener.CLOSE)
    }

    override fun reset() {
        super.reset()
        this.isRegionChanging = false
        chatMessage = null
    }

    override val isRunning: Boolean
        get() = settings.running


    fun nextAppearanceTicket(): Int {
        if (++appearanceTicketCounter == 0)
            appearanceTicketCounter = 1
        return appearanceTicketCounter
    }

    override fun getClientIndex(): Int = index.plus(32768)
}
