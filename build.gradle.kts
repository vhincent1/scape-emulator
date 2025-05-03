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

        testImplementation("junit:junit:4.13.1")
    }
}
