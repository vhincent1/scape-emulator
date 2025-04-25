package net.scapeemulator.game.net.world

class World(val id: Int, val flags: Int, val country: Int, val activity: String, val ip: String) {
    companion object {
        const val FLAG_MEMBERS: Int = 0x1
        const val FLAG_QUICK_CHAT: Int = 0x2
        const val FLAG_PVP: Int = 0x4
        const val FLAG_LOOT_SHARE: Int = 0x8
        const val FLAG_HIGHLIGHT: Int = 0x10
    }
}
