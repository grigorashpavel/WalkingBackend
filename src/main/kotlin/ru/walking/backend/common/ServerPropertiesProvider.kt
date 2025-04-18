package ru.walking.backend.common

import jakarta.el.PropertyNotFoundException
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.stereotype.Component

@Component
@Suppress("UseDataClass")
class ServerPropertiesProvider(private val serverProperties: ServerProperties) {
    val contextPath get() = serverProperties
        .servlet
        .contextPath
        ?: throw PropertyNotFoundException("Property context path not found")
}

@Component
@Suppress("UseDataClass")
class ServerEndpointsProvider {
    val authPath = "/v1/start-session"
}
