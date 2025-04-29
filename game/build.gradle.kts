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

//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    implementation("com.fasterxml.jackson.core:jackson-core:2.18.0")
    implementation("org.javassist:javassist:3.30.2-GA")

//    implementation(project(":web"))
//    runtimeOnly("io.ktor:ktor-server-core:2.3.1")
//    implementation("io.ktor:ktor-server-netty:2.3.1")
//    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")
//    implementation("io.ktor:ktor-server-content-negotiation:2.2.1")
}
description = "ScapeEmulator Game Server"

application {
    mainClass.set("net.scapeemulator.game.GameServer")
}

tasks.jar {
    println("shadowJar")
    manifest.attributes["Main-Class"] ="net.scapeemulator.game.GameServer"
}