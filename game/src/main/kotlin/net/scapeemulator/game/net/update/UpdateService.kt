package net.scapeemulator.game.net.update

import java.util.Queue
import java.util.ArrayDeque


class UpdateService : Runnable {
    private val pendingSessions: Queue<UpdateSession> = ArrayDeque<UpdateSession>()

    fun addPendingSession(session: UpdateSession) {
        synchronized(pendingSessions) {
            pendingSessions.add(session)
            (pendingSessions as Object).notifyAll()
        }
    }

    override fun run() {
        while (true) {
            var session: UpdateSession?//check

            synchronized(pendingSessions) {
                while ((pendingSessions.poll().also { session = it }) == null) {
                    try {
                        (pendingSessions as Object).wait()
                    } catch (e: InterruptedException) {
                        /* ignore */
                    }
                }
            }

            session!!.processFileQueue()
        }
    }
}
