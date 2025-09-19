pluginManagement {
    includeBuild("build-logic")
    repositories {
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}
rootProject.name = "chat_backend"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("app")
include("user")
include("chat")
include("notification")
include("common")