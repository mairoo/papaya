package kr.co.pincoin.api.global.config

import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("prod")
class DisableSwaggerConfig {
    @Bean
    fun disableSwagger(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.paths.clear()
            openApi.components.schemas.clear()
        }
    }
}