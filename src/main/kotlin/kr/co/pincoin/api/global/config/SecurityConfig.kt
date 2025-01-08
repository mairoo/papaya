package kr.co.pincoin.api.global.config

import kr.co.pincoin.api.global.security.DjangoPasswordEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${web-config.cors.allowed-origins:*}")
    private val allowedOrigins: String,

    @Value("\${web-config.cors.allowed-methods:*}")
    private val allowedMethods: String,

    @Value("\${web-config.cors.allowed-headers:*}")
    private val allowedHeaders: String,

    @Value("\${web-config.cors.max-age:3600}")
    private val maxAge: Long,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .cors { cors ->
            cors.configurationSource(corsConfigurationSource())
        }

        .headers { headers ->
            // 1-1. 기본 헤더 비활성화 후 필요한 것만 설정
            headers.defaultsDisabled()

            headers.httpStrictTransportSecurity { hstsConfig ->
                hstsConfig
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                    .preload(true)
            }

            headers.contentTypeOptions { }
            headers.cacheControl { }
        }
        .csrf { it.disable() }
        .formLogin { it.disable() }
        .httpBasic { it.disable() }
        .rememberMe { it.disable() }
        .anonymous { it.disable() }
        .sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests { auth ->
            auth
                .requestMatchers("/actuator/**").denyAll()
                .requestMatchers(
                    "/auth/**",
                    "/oauth2/**",
                    "/products/**",
                    "/categories/**",
                    "/public/**",
                    "/api/**",
                    "/payment/bank-transfer/callback",
                    "/payment/billgate/callback",
                    "/payment/paypal/callback",
                    "/payment/danal/callback"
                ).permitAll()
                .anyRequest().authenticated()
        }
//        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
//        .exceptionHandling { exception ->
//            exception
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler)
//        }
        .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val originPatterns = allowedOrigins.split(",")
        val methods = allowedMethods.split(",")
        val headers = allowedHeaders.split(",")
        val age = maxAge

        val configuration = CorsConfiguration().apply {
            exposedHeaders = listOf("Set-Cookie")
            allowCredentials = true
            allowedOriginPatterns = originPatterns
            allowedMethods = methods
            allowedHeaders = headers
            maxAge = age
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = DjangoPasswordEncoder()
}