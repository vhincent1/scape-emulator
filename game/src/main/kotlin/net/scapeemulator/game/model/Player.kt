package net.scapeemulator.game.model

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.net.game.GameSession

//todo Player(world, account, session)
class Player : Mob() {
    var worldId = 0
    var databaseId: Int = 0
    var session: GameSession? = null

    lateinit var username: String
    lateinit var password: String
    var rights: Int = 0

    val localPlayers: MutableList<Player> = ArrayList()
    val localNpcs: MutableList<Npc> = ArrayList()

    val inventory: Inventory = Inventory(28)
    val equipment: Inventory = Inventory(14)
    val bank: Inventory = Inventory(496, Inventory.StackMode.ALWAYS)
    var chatMessage: ChatMessage? = null
    val settings: PlayerSettings = PlayerSettings(this)
    val interfaceSet: InterfaceSet = InterfaceSet(this)

    var runScript: RunScript? = null

    val appearanceTickets: IntArray = IntArray(World.MAX_PLAYERS)
    private var appearanceTicketCounter = 0
    var appearanceTicket: Int = nextAppearanceTicket()
        private set

    private fun nextAppearanceTicket(): Int {
        if (++appearanceTicketCounter == 0) appearanceTicketCounter = 1
        return appearanceTicketCounter
    }

    fun appearanceUpdated() {
        appearanceTicket = nextAppearanceTicket()
    }

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

    fun send(message: Message): ChannelFuture? = session?.send(message)
    fun sendMessage(text: String) = send(ServerMessage(text))
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

    override val isRunning: Boolean get() = settings.running
    override fun getClientIndex(): Int = index.plus(32768)
}
