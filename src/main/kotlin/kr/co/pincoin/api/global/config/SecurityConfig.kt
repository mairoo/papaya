package kr.co.pincoin.api.global.config

import kr.co.pincoin.api.global.security.handler.ApiAuthenticationEntryPoint
import kr.co.pincoin.api.global.security.password.DjangoPasswordEncoder
import kr.co.pincoin.api.global.security.properties.CorsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationEntryPoint: ApiAuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler,
    private val corsProperties: CorsProperties,
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
        .exceptionHandling { exception ->
            exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        }
        .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            exposedHeaders = listOf("Set-Cookie")
            allowCredentials = true

            allowedOriginPatterns = corsProperties.allowedOrigins.split(",")
            allowedMethods = corsProperties.allowedMethods.split(",")
            allowedHeaders = corsProperties.allowedHeaders.split(",")
            maxAge = corsProperties.maxAge
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = DjangoPasswordEncoder()
}