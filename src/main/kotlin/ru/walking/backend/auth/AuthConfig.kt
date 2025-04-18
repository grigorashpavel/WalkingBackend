package ru.walking.backend.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import ru.walking.backend.common.CommonHeaders
import ru.walking.backend.filters.HeadersValidationFilter
import ru.walking.backend.filters.SessionValidationFilter

@Configuration
@EnableWebSecurity
class AuthConfig(
    private val sessionValidationFilter: SessionValidationFilter,
    private val headersValidationFilter: HeadersValidationFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(headersValidationFilter, BasicAuthenticationFilter::class.java)
            .addFilterBefore(sessionValidationFilter, BasicAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigSource(): UrlBasedCorsConfigurationSource {
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration(
                "/**",
                CorsConfiguration().apply {
                    allowedOrigins = listOf("*")
                    allowedMethods = listOf("*")
                    allowedHeaders = listOf("*")
                    exposedHeaders = listOf(CommonHeaders.SESSION_ID)
                    allowCredentials = true
                }
            )
        }
    }
}
