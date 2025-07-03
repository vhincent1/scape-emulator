package net.scapeemulator.game.util.math

/**
 * Class to handle some commonly used math methods.
 *
 * @author David Insley
 */
object BasicMath {
    /**
     * Adds two integers together and compares it against Integer.MAX_VALUE
     *
     * @param i1 first integer
     * @param i2 second integer
     * @return the amount of overflow, or 0 if the two integers can be added with no overflow
     */
    fun integerOverflow(i1: Int, i2: Int): Int {
        require(!(i1 < 0 || i2 < 0)) { "checking for overflow with negative numbers" }
        val l = i1.toLong() + i2
        if (l <= Int.MAX_VALUE) return 0
        return (l - Int.MAX_VALUE).toInt()
    }
}