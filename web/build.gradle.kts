plugins {
    kotlin("jvm")
}
println("web")
dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.10")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.10")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.10")
    implementation("ch.qos.logback:logback-classic:1.4.14") // Logging
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.10")
}

description = "Web Server"
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(23)
}