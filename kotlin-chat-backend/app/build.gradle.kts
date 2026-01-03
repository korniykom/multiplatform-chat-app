plugins {
    id("kotlin-chat.spring-boot-app")
}

group = "com.korniykom"
version = "0.0.1-SNAPSHOT"
description = "Backend for chat using kotlin"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.mail)
    implementation(libs.kotlin.reflect)
    implementation(libs.spring.boot.starter.redis)
    implementation(libs.spring.boot.starter.amqp)
    runtimeOnly(libs.postgresql)

    implementation(projects.user)
    implementation(projects.chat)
    implementation(projects.notification)
    implementation(projects.common)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

