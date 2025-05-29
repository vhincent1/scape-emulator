package net.scapeemulator.game.pathfinder

/**
 * Represents a node used by the A* algorithm.
 *
 * @author Graham Edgecombe
 */
class Node
/**
 * Creates a node.
 *
 * @param x
 * The x coordinate.
 * @param y
 * The y coordinate.
 */(
    /**
     * The x coordinate.
     */
    val x: Int,
    /**
     * The y coordinate.
     */
    val y: Int
) : Comparable<Node?> {
    /**
     * Gets the parent node.
     *
     * @return The parent node.
     */
    /**
     * Sets the parent.
     *
     * @param parent
     * The parent.
     */
    /**
     * The parent node.
     */
    var parent: Node? = null

    /**
     * The cost.
     */
    var cost: Int = 0

    /**
     * The heuristic.
     */
    private val heuristic = 0

    /**
     * The depth.
     */
    private val depth = 0

    /**
     * Gets the X coordinate.
     *
     * @return The X coordinate.
     */

    /**
     * Gets the Y coordinate.
     *
     * @return The Y coordinate.
     */

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + cost
        result = prime * result + depth
        result = prime * result + heuristic
        result = prime * result + (if ((parent == null)) 0 else parent.hashCode())
        result = prime * result + x
        result = prime * result + y
        return result
    }

    override fun compareTo(other: Node?): Int {
        return cost - other!!.cost
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val other = other as Node
        if (cost != other.cost) return false
        if (depth != other.depth) return false
        if (heuristic != other.heuristic) return false
        if (parent == null) {
            if (other.parent != null) return false
        } else if (parent != other.parent) return false
        if (x != other.x) return false
        if (y != other.y) return false
        return true
    }

}