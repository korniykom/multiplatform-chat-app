plugins {

    id("java-library")
    id("kotlin-chat.spring-boot-service")
    alias(libs.plugins.kotlin.jpa)
}

group = "com.korniykom"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.common)
    testImplementation(kotlin("test"))
    implementation(libs.spring.boot.starter.amqp)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}