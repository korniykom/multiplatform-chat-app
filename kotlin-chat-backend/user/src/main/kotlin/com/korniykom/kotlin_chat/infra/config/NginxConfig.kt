package com.korniykom.kotlin_chat.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "nginx")
data class NginxConfig(
    var trustedIp: List<String> = emptyList(),
    var requireProxy: Boolean = true
)
