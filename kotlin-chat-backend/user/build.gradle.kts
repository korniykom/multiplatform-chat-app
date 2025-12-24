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
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.jwt.api)

    implementation(projects.common)


    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}