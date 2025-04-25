rootProject.name = "scapeemulator"
pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
}

include(listOf("cache", "login", "game", "util", "flooder"))