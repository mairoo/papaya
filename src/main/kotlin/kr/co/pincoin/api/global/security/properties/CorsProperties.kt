package kr.co.pincoin.api.global.security.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "web-config.cors")
data class CorsProperties(
    val allowedOrigins: String = "*",
    val allowedMethods: String = "*",
    val allowedHeaders: String = "*",
    val maxAge: Long = 3600
)