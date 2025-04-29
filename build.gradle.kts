plugins {
    kotlin("jvm")
}
//println("hieeee")
allprojects {
    group = "net.scapeemulator"
    version = "1.0.0-SNAPSHOT"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    kotlin {
        jvmToolchain(23)
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        //logger
        api("com.michael-bull.kotlin-inline-logger:kotlin-inline-logger:1.0.6")
        api("org.slf4j:slf4j-simple:2.0.5")
//        api("org.slf4j:slf4j-api:2.0.12")
//        api("org.slf4j:slf4j-jdk14:1.7.5")
        testImplementation("junit:junit:4.13.1")
    }
}
