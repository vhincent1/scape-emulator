plugins {
    kotlin("jvm")
    application
}

dependencies {
    api(project(":cache"))
    api(project(":util"))
    api(libs.io.netty.netty.transport)
    api(libs.io.netty.netty.handler)
    api(libs.io.netty.netty.codec.http)
    api(libs.mysql.mysql.connector.java)
    api(libs.org.mindrot.jbcrypt)
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.0")
}
description = "ScapeEmulator Game Server"

application {
    mainClass.set("net.scapeemulator.game.GameServer")
}