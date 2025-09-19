plugins {
    id("java-library")
    id("chat_backend.spring-boot-service")
    kotlin("plugin.jpa")

}
group = "org.korniykom"
version = "unspecified"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation(projects.common)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}