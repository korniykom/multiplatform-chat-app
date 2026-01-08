package com.korniykom.kotlin_chat.api.security

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths


@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val avatarsPath = Paths.get("uploads/avatars").toAbsolutePath().toUri().toString()
        registry.addResourceHandler("/uploads/avatars/**")
            .addResourceLocations(avatarsPath)
    }
}