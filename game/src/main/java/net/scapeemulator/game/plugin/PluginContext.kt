/**
 * scape-emulator-final
 * Copyright (c) 2014 Justin Conner
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in  the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http:></http:>//www.gnu.org/license/>.
 */
package net.scapeemulator.game.plugin

import net.scapeemulator.game.button.ButtonDispatcher
import net.scapeemulator.game.button.ButtonHandler
import net.scapeemulator.game.command.CommandDispatcher
import net.scapeemulator.game.command.CommandHandler
import java.util.*

/**
 * Created by Hadyn Richard
 */
class PluginContext
/**
 * Constructs a new [PluginContext];
 */
{
    /**
     * The list of button handlers.
     */
    private val buttonHandlers: MutableList<ButtonHandler> = LinkedList<ButtonHandler>()

    /**
     * The list of command handlers.
     */
    private val commandHandlers: MutableList<CommandHandler> = LinkedList<CommandHandler>()

    /**
     * Adds a button dispatcher decorator to the list of decorators.
     * @param handler The button handler to add.
     */
    fun addButtonHandler(handler: ButtonHandler) {
        buttonHandlers.add(handler)
    }

    /**
     * Decorates a button dispatcher with all the button handlers registered to the context.
     * @param dispatcher The dispatcher to decorate.
     */
    fun decorateButtonDispatcher(dispatcher: ButtonDispatcher) {
        for (handler in buttonHandlers) {
            dispatcher.bind(handler)
        }
    }

    /**
     * Adds a button dispatcher decorator to the list of decorators.
     * @param handler The button handler to add.
     */
    fun addCommandHandler(handler: CommandHandler) {
        commandHandlers.add(handler)
    }

    /**
     * Decorates a button dispatcher with all the button handlers registered to the context.
     * @param dispatcher The dispatcher to decorate.
     */
    fun decorateCommandDispatcher(dispatcher: CommandDispatcher) {
        for (handler in commandHandlers) {
            dispatcher.bind(handler)
        }
    }
}