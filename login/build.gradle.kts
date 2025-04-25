plugins {
    kotlin("jvm")
}
println("Login")
dependencies {
    api(project(":util"))
    api(libs.io.netty.netty.transport)
    api(libs.io.netty.netty.handler)
//    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
}

description = "ScapeEmulator Login Server"
//repositories {
//    mavenCentral()
//}
//kotlin {
//    jvmToolchain(23)
//}