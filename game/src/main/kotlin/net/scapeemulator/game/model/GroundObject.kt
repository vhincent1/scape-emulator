package net.scapeemulator.game.model

class GroundObject(val id: Int, val type: Int, override var position: Position, val rotation: Int) : Entity()