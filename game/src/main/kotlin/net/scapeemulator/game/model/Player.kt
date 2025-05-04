package net.scapeemulator.game.model

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import net.scapeemulator.game.msg.*
import net.scapeemulator.game.net.game.GameSession

class Player : Mob() {
    var session: GameSession? = null

    var databaseId: Int = 0

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
    val appearanceTickets: IntArray = IntArray(World.MAX_PLAYERS)
    var appearanceTicketCounter = 0

    var appearanceTicket: Int = nextAppearanceTicket()
        private set

    var appearance: Appearance = Appearance.DEFAULT_APPEARANCE
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

    private fun init() {
        skillSet.addListener(SkillMessageListener(this))
        skillSet.addListener(SkillAppearanceListener(this))

        inventory.addListener(InventoryMessageListener(this, 149, 0, 93))
        inventory.addListener(InventoryFullListener(this, "inventory"))

        bank.addListener(InventoryFullListener(this, "bank"))

        equipment.addListener(InventoryMessageListener(this, 387, 28, 94))
        equipment.addListener(InventoryFullListener(this, "equipment"))
        equipment.addListener(InventoryAppearanceListener(this))
        // add equip listener
//        equipment.addListener(object : InventoryListener {
//            override fun itemChanged(
//                inventory: Inventory,
//                slot: Int,
//                item: Item?
//            ) {
//                println("equip listener: " + item.toString())
//            }
//
//            override fun itemsChanged(inventory: Inventory) {
//                println("Item: " + inventory.toString())
//            }
//
//            override fun capacityExceeded(inventory: Inventory) {
//                println("Item: " + inventory.toString())
//            }
//
//        })
    }

    fun send(message: Message): ChannelFuture? {
        return session?.send(message)
    }

    fun sendMessage(text: String) {
        send(ServerMessage(text))
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
        get() = settings.running

    fun nextAppearanceTicket(): Int {
        if (++appearanceTicketCounter == 0)
            appearanceTicketCounter = 1
        return appearanceTicketCounter
    }

}
