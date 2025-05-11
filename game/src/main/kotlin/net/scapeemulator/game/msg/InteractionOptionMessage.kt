package net.scapeemulator.game.msg

class InteractionOptionMessage(@JvmField val index: Int,  @JvmField val name: String) : Message()
class PlayerInteractionMessage(@JvmField val target: Int, @JvmField val index: Int) : Message()
