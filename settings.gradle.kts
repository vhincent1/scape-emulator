rootProject.name = "scapeemulator"
pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
}

include(listOf("cache", "login", "game", "util", "flooder"))
//include("web")
//include("application")
include("game-ktor", "cache-lib")


//v1
//include(listOf("game", "cache", "util"))