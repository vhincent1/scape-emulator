package net.scapeemulator.game.button

import net.scapeemulator.game.model.Interface
import net.scapeemulator.game.model.Player
import net.scapeemulator.game.msg.RegionChangeMessage

class WorldMapButtonHandler : ButtonHandler(Interface.WORLD_MAP) {
    override fun handle(player: Player, slot: Int, parameter: Int) {
        if (slot == 3) {
            /*
             * TODO: this is probably the incorrect script to run, and
             * definitely incorrect arguments!
             *
             * Vastico dumped 140 as the script to run upon closing the world
             * map. This script does not seem to (directly) end up using opcode
             * 6002 anywhere, which sets a flag allowing the map region to be
             * re-loaded even though your region X/Y have not changed.
             *
             * Scripts 1187 and 1132 both contain some code that uses this
             * opcode, and seem to base the value they pass it off a weird
             * interface-related calculation. Passing 0 and 0 seem to make the
             * flag get set so this code works, but this is a nasty hack that
             * needs fixing!!! It is likely that there are some unintended side
             * effects.
             */
//            player.send(ScriptMessage(1187, "ii", 0, 0))
            val position = player.position

            player.lastKnownRegion = position
            player.send(RegionChangeMessage(position))

            player.interfaceSet.closeFullscreen()

//            println("dp:"+player.interfaceSet.displayMode)
//            player.send(InterfaceRootMessage(548, 2))
//            player.send(ScriptMessage(101, ""))
        }
    }
}
