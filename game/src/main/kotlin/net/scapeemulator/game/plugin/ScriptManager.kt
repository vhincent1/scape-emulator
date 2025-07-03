package net.scapeemulator.game.plugin

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngineManager


class ScriptManager() {
    private val logger = KotlinLogging.logger { }
    private val scripts: MutableMap<String, CompiledScript> = HashMap()
    private val manager = ScriptEngineManager()
    private val engine = manager.getEngineByExtension("kts")

    init {
//        with(engine) {
//            put("SERVER", server)
//            put("PLUGIN_CONTEXT", pluginContext)
//        }
    }

    fun load(directory: File) {
        val compilable = engine as Compilable
        directory.walk().forEach { file ->
            if (file.isDirectory || file.extension != "kts") return@forEach
            val scriptName = file.name
            val compiledScript = compilable.compile(file.reader())
            scripts[scriptName] = compiledScript
        }
        logger.info { "Loaded  ${scripts.size} scripts" }
        scripts.forEach { (name, script) -> script.eval() }
    }
}