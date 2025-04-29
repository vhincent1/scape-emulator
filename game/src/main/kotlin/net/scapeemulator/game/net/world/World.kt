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

class Country(val flag: Int, val name: String) {
    companion object {
        const val FLAG_UK: Int = 77
        const val FLAG_USA: Int = 225
        const val FLAG_CANADA: Int = 38
        const val FLAG_NETHERLANDS: Int = 161
        const val FLAG_AUSTRALIA: Int = 16
        const val FLAG_SWEDEN: Int = 191
        const val FLAG_FINLAND: Int = 69
        const val FLAG_IRELAND: Int = 101
        const val FLAG_BELGIUM: Int = 22
        const val FLAG_NORWAY: Int = 162
        const val FLAG_DENMARK: Int = 58
        const val FLAG_BRAZIL: Int = 31
        const val FLAG_MEXICO: Int = 152
    }
}