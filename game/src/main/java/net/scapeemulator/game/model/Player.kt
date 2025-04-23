package net.scapeemulator.game.model

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.net.game.GameSession

class Player : Mob() {
    var databaseId: Int = 0
    lateinit var username: String
    lateinit var password: String
    var rights: Int = 0
    lateinit var session: GameSession
    var isRegionChanging: Boolean = false
        private set
    var lastKnownRegion: Position? = null
        set(lastKnownRegion) {
            field = lastKnownRegion
            this.isRegionChanging = true
        }
    val localPlayers: MutableList<Player> = ArrayList<Player>()
    val localNpcs: MutableList<Npc> = ArrayList<Npc>()
    var appearance: Appearance? = Appearance.DEFAULT_APPEARANCE
        set(appearance) {
            field = appearance
            this.appearanceTicket = nextAppearanceTicket()
        }
    var energy: Int = 100
        set(energy) {
            field = energy
            this.send(EnergyMessage(energy))
        }

    val inventory: Inventory = Inventory(28)
    val equipment: Inventory = Inventory(14)
    val bank: Inventory = Inventory(496, Inventory.StackMode.ALWAYS)
    var chatMessage: ChatMessage? = null
    val settings: PlayerSettings = PlayerSettings(this)
    val interfaceSet: InterfaceSet = InterfaceSet(this)
    val appearanceTickets: IntArray = IntArray(World.MAX_PLAYERS)
    var appearanceTicket: Int = nextAppearanceTicket()
        private set

    init {
        init()
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

    fun send(message: Message): ChannelFuture? = session.send(message)
    fun sendMessage(text: String) = send(ServerMessage(text))

    val isChatUpdated: Boolean
        get() = chatMessage != null

    val stance: Int
        get() {
            val weapon = equipment.get(Equipment.WEAPON)
            return weapon?.equipmentDefinition?.getStance() ?: 1426
        }

    fun logout() {
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
        get() = settings.isRunning()

    companion object {
        private var appearanceTicketCounter = 0

        private fun nextAppearanceTicket(): Int {
            if (++appearanceTicketCounter == 0) appearanceTicketCounter = 1

            return appearanceTicketCounter
        }
    }
}
