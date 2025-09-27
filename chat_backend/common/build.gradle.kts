plugins {
    id("java-library")
    id("chat_backend.kotlin-common")
    kotlin("plugin.jpa")

}
group = "com.korniykom"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api(libs.jackson.module.kotlin)
    api(libs.kotlin.reflect)

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}