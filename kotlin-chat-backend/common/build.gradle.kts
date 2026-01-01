plugins {
    id("java-library")
    id("kotlin-chat.kotlin-common")
}

group = "com.korniykom"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jackson.module.kotlin)
    api(libs.kotlin.reflect)

    implementation(libs.spring.boot.starter.amqp)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}