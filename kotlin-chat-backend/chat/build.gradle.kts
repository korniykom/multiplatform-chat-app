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
    runtimeOnly(libs.postgresql)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}