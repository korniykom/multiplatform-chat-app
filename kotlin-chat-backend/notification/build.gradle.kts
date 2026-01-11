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
    implementation(libs.firebase.admin.sdk)
    implementation(projects.common)
    testImplementation(kotlin("test"))
    implementation(libs.spring.boot.starter.amqp)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.mail)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.data.jpa)
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}