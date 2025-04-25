plugins{
    kotlin("jvm")
}
println("hieeee")
allprojects {
    group = "net.scapeemulator"
    version = "1.0.0-SNAPSHOT"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        api("org.slf4j:slf4j-api:1.7.5")
        api("org.slf4j:slf4j-jdk14:1.7.5")
        testImplementation("junit:junit:4.11")
    }
}
