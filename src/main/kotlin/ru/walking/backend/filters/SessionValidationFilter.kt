package ru.walking.backend.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.walking.backend.common.CommonHeaders
import ru.walking.backend.common.ServerEndpointsProvider
import ru.walking.backend.service.SessionService

@Component
class SessionValidationFilter(
    private val sessionService: SessionService,
    private val endpointsProvider: ServerEndpointsProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath == endpointsProvider.authPath) {
            return filterChain.doFilter(request, response)
        }

        val deviceId = request.getHeader(CommonHeaders.DEVICE_ID)
        val clientVersion = request.getHeader(CommonHeaders.CLIENT_VERSION)
        val sessionKey = request.getHeader(CommonHeaders.SESSION_ID)
        if (deviceId == null || clientVersion == null || sessionKey == null) {
            return
        }

        if (!sessionService.validateSession(sessionKey, deviceId, clientVersion)) {
            return response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }

        filterChain.doFilter(request, response)
    }
}
