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

import javax.script.*

/**
 * Created by Hadyn Richard
 */
class ScriptEnvironment : PluginEnvironment {
    /**
     * The collection of scripts that have been compiled.
     */
    private val scripts: MutableMap<String, CompiledScript?> = HashMap()

    /**
     * The script engine for this environment.
     */
    private var engine: ScriptEngine? = manager.getEngineByName("javascript")

    /**
     * Constructs a new [ScriptEnvironment];
     */
    init {
        if (engine == null) throw NullPointerException()
    }

    /**
     * Sets the context of the script environment.
     *
     * @param context   The environment context.
     */
    override fun setContext(context: PluginContext) {
        engine?.getBindings(ScriptContext.GLOBAL_SCOPE)?.put("ctx", context)
    }

    /**
     * Evalulates a script, the script must have been previously compiled.
     *
     * @param name              The name of the script to evaluate.
     * @throws ScriptException  A script exception was thrown while evaluating the script.
     */
    @Throws(ScriptException::class)
    fun eval(name: String) {
        val compiledScript: CompiledScript? = scripts[name]
        if (compiledScript == null) {
            throw RuntimeException("script does not exist")
        }
        compiledScript.eval()
    }

    /**
     * Loads a script.
     *
     * @param script            The script to load.
     * @throws ScriptException  A script exception was thrown while evaluating the script.
     */
    @Throws(ScriptException::class)
    fun load(script: Script) {
        val compilable = engine as Compilable
        val compiledScript = compilable.compile(script.source)
        scripts.put(script.name, compiledScript)
    }

    /**
     * Evaluates a script.
     *
     * @param script            The script to evaluate.
     * @throws ScriptException  A script exception was thrown while evaluating the script.
     */
    @Throws(ScriptException::class)
    fun eval(script: Script) {
        if (scripts.containsKey(script.name)) {
            eval(script.name)
        } else {
            val compilable = engine as Compilable
            val compiledScript = compilable.compile(script.source)
            scripts.put(script.name, compiledScript)
            compiledScript.eval()
        }
    }

    companion object {
        /**
         * The script engine manager for all the script environments.
         */
        private val manager = ScriptEngineManager()
    }
}