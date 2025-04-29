/**
 * scape-emulator-final
 * Copyright (c) 2014 Justin Conner
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * This program is distributed in  the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http:></http:>//www.gnu.org/license/>.
 */
package net.scapeemulator.game.plugin

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.github.michaelbull.logging.InlineLogger
import java.io.File
import java.io.IOException
import java.util.*
import javax.script.ScriptException

//import net.scapeemulator.game.plugin.impl.RubyEnvironment;

/**
 * Created by Hadyn Richard
 */
class PluginLoader(context: PluginContext) {
    /**
     * The JSON factory to create new JSON parsers with.
     */
    private val factory = JsonFactory()

    /**
     * The ruby script environment for the plugin loader.
     */
    private val scriptEnvironment: ScriptEnvironment = ScriptEnvironment()

    /**
     * The map that contains all the parsed plugin data.
     */
    private val parsedPluginData: MutableMap<String?, Plugin?> = HashMap<String?, Plugin?>()

    /**
     * The set of plugins that have had their scripts loaded.
     */
    private val loadedPlugins: MutableSet<String?> = HashSet<String?>()

    /**
     * Constructs a new [PluginLoader];
     */
    init {
        scriptEnvironment.setContext(context)
    }

    /**
     * Loads all the plugins from the specified directory.
     */
    @Throws(IOException::class, ScriptException::class)
    fun load(dir: String) {
        load(File(dir))
    }

    /**
     * Loads all the plugins from the specified file directory.
     */
    @Throws(IOException::class, ScriptException::class)
    fun load(dir: File) {
        scriptEnvironment.eval(Script(File(dir, "bootstrap.rb")))
//        dir.listFiles { _, name -> name.endsWith(".js") }
        for (file in dir.listFiles()) {

            /* Skip over non-directory files */

            if (!file.isDirectory()) {
                continue
            }

            val dataFile = File(file, "plugin.json")

            /* Check to see if the json data file exists */
            if (!dataFile.exists()) {
                logger.warn { "missing plugin.json file from '" + file.getName() + "' plugin" }
                return
            }

            val parser: JsonParser = factory.createJsonParser(dataFile)

            /* Check to see if the JSON structure start is correct */
            if (parser.nextToken() !== JsonToken.START_OBJECT)
                throw IOException()


            /* Load the plugin data from the JSON file */
            val plugin = Plugin()
            while (parser.nextToken() !== JsonToken.END_OBJECT) {
                val currentName: String = parser.currentName
                when (currentName.lowercase(Locale.getDefault())) {
                    "scripts" -> {
                        /* Check to see if the next token is valid */
                        if (parser.nextToken() !== JsonToken.START_ARRAY) {
                            throw IOException()
                        }

                        while (parser.nextToken() !== JsonToken.END_ARRAY) {
                            plugin.addScript(parser.text)
                        }
                    }

                    "dependencies" -> {
                        /* Check to see if the next token is valid */
                        if (parser.nextToken() !== JsonToken.START_ARRAY) {
                            throw IOException()
                        }

                        while (parser.nextToken() !== JsonToken.END_ARRAY) {
                            plugin.addDependency(parser.getText())
                        }
                    }
                }
            }
            parsedPluginData.put(file.getName(), plugin)
        }

        /* Load each of the plugins from its data */
        for (entry in parsedPluginData.entries) {
            /* Check if the plugin has already been loaded */
            if (loadedPlugins.contains(entry.key)) {
                continue
            }

            loadPlugin(dir, entry.key!!, entry.value!!)
        }

        logger.info { "PluginLoader loaded " + loadedPlugins.size + " plugins..." }
    }

    /**
     * Loads a plugin from the specified root directory, name, and plugin data.
     */
    @Throws(IOException::class, ScriptException::class)
    private fun loadPlugin(dir: File, name: String, data: Plugin) {
        /* Check if the plugin has already been loaded */
        if (loadedPlugins.contains(name)) {
            return
        }

        /* Load all of the dependencies first */
        for (pluginName in data.dependencies) {
            /* Check if the dependency is valid */

            if (!parsedPluginData.containsKey(pluginName)) {
                continue
            }

            loadPlugin(dir, pluginName, parsedPluginData[pluginName]!!)
        }

        val scriptDir = File(dir, name)

        /* Evaluate each of the scripts */
        for (scriptName in data.scripts) {
            scriptEnvironment.eval(Script(File(scriptDir, scriptName)))
        }

        /* Note that the plugin has been loaded */
        loadedPlugins.add(name)
    }

    companion object {
        /**
         * The logger for this class.
         */
        private val logger = InlineLogger(PluginLoader::class)
    }
}