package net.scapeemulator.game.cache

import net.scapeemulator.cache.def.ObjectDefinition
import net.scapeemulator.game.model.*
import net.scapeemulator.game.pathfinder.TraversalMap

abstract class MapListener {
    abstract fun tileDecoded(flags: Int, position: Position)
    abstract fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position)
}

class GroundObjectPopulator(world: World) : MapListener() {
    private val regionManager = world.region
    override fun tileDecoded(flags: Int, position: Position) {}
    override fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position) {
        if (!regionManager.isRegionInitialized(position))
            regionManager.initializeRegion(position)

//        val region = regionManager.getRegion(position) ?: return
//        val gameObject = GameObject(id, type.id, position, rotation)
//        region.addObject(gameObject)
    }

}

class TraversalMapListener(private val map: TraversalMap) : MapListener() {
    override fun tileDecoded(flags: Int, position: Position) {
        //todo member zones see @arios @MapZone @ZoneRestriction
        if ((flags and FLAG_BRIDGE) != 0)
            map.markBridge(position.height, position.x, position.y)
        if ((flags and FLAG_CLIP) != 0)
            map.markBlocked(position.height, position.x, position.y)
    }

    override fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position) {
        val def: ObjectDefinition = ObjectDefinitions.forId(id) ?: return
        if (!def.isSolid) return

        if (type.isWall)  // type 0-3
            map.markWall(rotation, position.height, position.x, position.y, type, def.isImpenetrable)

        if (type.id in 9..12) {
            /* Flip the length and width if the object is rotated */
            var width: Int = def.width
            var length: Int = def.length
            if (rotation == 1 || rotation == 3) {
                width = def.length
                length = def.width
            }
            map.markOccupant(position.height, position.x, position.y, width, length, def.isImpenetrable)
        }
    }

    companion object {
        private const val FLAG_CLIP = 0x1
        private const val FLAG_BRIDGE = 0x2
    }
}

class RegionMapListener(private val manager: RegionManager) : MapListener() {

    override fun tileDecoded(flags: Int, position: Position) {
        if (!manager.isRegionInitialized(position)) manager.initializeRegion(position)
    }

    override fun objectDecoded(id: Int, rotation: Int, type: ObjectType, position: Position) {
        if (!manager.isRegionInitialized(position)) manager.initializeRegion(position)
        val region: Region = manager.getRegion(position.x, position.y) ?: return
        region.addObject(GameObject(id, type.id, position, rotation))
    }
}