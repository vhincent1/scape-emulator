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

    // logger
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.slf4j:slf4j-api:2.0.17")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.13") // or another SLF4J implementation

    // json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")

    // script engine
    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
}
description = "ScapeEmulator Game Server"

application {
    mainClass.set("net.scapeemulator.game.GameServer")
    applicationDefaultJvmArgs = listOf("-Xms1024m", "-Xmx1048m")
}

tasks.run.get().workingDir = File("src/main/resources")

tasks.jar {
    println("shadowJar")
    manifest.attributes["Main-Class"] = "net.scapeemulator.game.GameServer"
}
