package net.scapeemulator.game.model

import java.text.SimpleDateFormat
import java.util.*

/**
 * Membership system model
 *
 * @author my-swagger
 * @version 1.0
 */
class Membership {

    /**
     * If the user is a member
     */
    private var member = false
        get() {
            update()
            return field
        }

    /**
     * The Calendar to represent the time membership is valid to
     */
    private val membership: Calendar = GregorianCalendar()

    /**
     * Updates the users membership status.
     */
    fun update() {
        if (member) {
            val current = Date()
            if (current.after(membership.getTime())) {
                member = false
            }
        }
    }

    /**
     * Extends the membership by the amount of days specified
     *
     * @param days The days to extend the membership by
     */
    fun extend(days: Int) {
        if (!member) {
            member = true
        }
        membership.add(Calendar.DATE, days)
    }

    /**
     * Cancels the users membership status and removes all the days
     * they have remaining
     */
    fun cancel() {
        if (member) {
            member = false
        }
        membership.clear()
    }

    val daysLeft: Int
        /**
         * Gets the amount of membership days the user has remaining
         *
         * @return The amount of days remaining
         */
        get() = ((membership.getTime().time - System.currentTimeMillis()) / (1000 * 60 * 60 * 24) + 1).toInt()

    val endTimeDate: String
        /**
         * Gets the membership as a date time
         *
         * @return The date as a String representative
         */
        get() = SimpleDateFormat("dd/MM/yy hh:mm:ss").format(membership.getTime())

}