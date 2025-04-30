plugins {
    kotlin("jvm")
    application
    id("com.gradleup.shadow") version "8.3.0"
}

dependencies {
    api(project(":cache"))
    api(project(":util"))

    api(libs.io.netty.netty.transport)
    api(libs.io.netty.netty.handler)
    api(libs.io.netty.netty.codec.http)
    api(libs.mysql.mysql.connector.java)
    api(libs.org.mindrot.jbcrypt)

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")
    implementation("org.javassist:javassist:3.30.2-GA")
}
description = "ScapeEmulator Game Server"

application {
    mainClass.set("net.scapeemulator.game.GameServer")
}

tasks.jar {
    println("shadowJar")
    manifest.attributes["Main-Class"] ="net.scapeemulator.game.GameServer"
}