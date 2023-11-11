package com.example.chaincuerealestate.configs

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@ConfigurationProperties(prefix = "chaincue-real-estate.cors")
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsConfig : WebMvcConfigurer {

    companion object {
        val ALLOWED_HEADERS: Array<String> = arrayOf("Authorization", "content-type")
        val ALLOWED_METHODS: Array<String> = arrayOf("GET", "PUT", "POST", "DELETE", "OPTIONS")
    }

    var allowedOrigin = "*"
    var maxAge = 3600

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*allowedOrigin.split(",").toTypedArray())
            .allowedMethods(*ALLOWED_METHODS)
            .allowedHeaders(*ALLOWED_HEADERS)
            .maxAge(maxAge.toLong())
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("*/**").setCacheControl(CacheControl.noCache())
    }
}
