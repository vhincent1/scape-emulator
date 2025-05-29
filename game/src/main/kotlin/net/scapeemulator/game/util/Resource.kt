package net.scapeemulator.game.util

import java.nio.charset.Charset

class Resource {
    private fun readResource(resource: String, charset: Charset = Charsets.UTF_8): String {
        val resourceUrl = javaClass.classLoader?.getResource(resource)
        return resourceUrl!!.readText(charset)
    }

    // only works inside a class
//    val lines = this::class.java.getResourceAsStream("file.txt")?.bufferedReader()?.readLines()

    // works otherwise
//    val lines = object {}.javaClass.getResourceAsStream("file.txt")?.bufferedReader()?.readLines()

//    fun loadResourcesFolder(folderPath: String) {
//        val classLoader = object {}.javaClass.classLoader
//        val resource = classLoader.getResource(folderPath)
//        if (resource != null) {
//            val folder = File(resource.path)
//            if (folder.exists() && folder.isDirectory) {
//                val files = folder.listFiles()
//                files?.forEach { file ->
//                    println("Resource File: ${file.name}")
//                }
//            }
//        } else {
//            println("Resource folder not found.")
//        }
//    }
//
//    fun main() {
//        loadResourcesFolder("my_resources_folder") // Replace with your resource folder path
//    }
}

/*
object Resource {
    val json = Json { allowStructuredMapKeys = true; ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T : Any> loadJsonResource(name: String, type: KClass<*>): T = type::class.java.getResourceAsStream(name)?.let { json.decodeFromStream<T>(it) } ?: throw IllegalStateException("Failed to load resource with name $name and type $type")
    inline fun <reified T : Any> loadYamlResource(name: String, type: KClass<*>): T = type::class.java.getResourceAsStream(name)?.let { Yaml.default.decodeFromStream<T>(it) } ?: throw IllegalStateException("Failed to load resource with name $name and type $type")

    inline fun <reified T> getResourcesDirectory(name: String): File {
        val resourceUrl = T::class.java.getResource(name) ?: throw IllegalStateException("Resource folder $name not found")

        val resourcesDirectory = File(resourceUrl.file)
        if (!resourcesDirectory.isDirectory) {
            throw IllegalStateException("Resource path $name does not point to a directory")
        }

        return resourcesDirectory
    }

    inline fun <reified T> parseYamlConfigFiles(name: String): List<T> {
        val directory = getResourcesDirectory<T>(name)

        val yaml = Yaml.default
        val configs = mutableListOf<T>()

        val npcFiles = directory.walkTopDown().filter { it.isFile && it.extension == "yaml" }

        for (file in npcFiles) {
            val yamlString = file.inputStream()
            val npcConfig = yaml.decodeFromStream<T>(yamlString)
            configs.add(npcConfig)
        }

        return configs
    }
}
 */