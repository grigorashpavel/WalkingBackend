package ru.walking.backend.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.walking.backend.common.CommonHeaders
import ru.walking.backend.common.ErrorMessages
import ru.walking.backend.common.ServerEndpointsProvider

@Component
class HeadersValidationFilter(
    private val endpointsProvider: ServerEndpointsProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        if (request.servletPath == endpointsProvider.authPath) {
            return chain.doFilter(request, response)
        }

        val missingHeaders = listOf(
            CommonHeaders.DEVICE_ID to request.getHeader(CommonHeaders.DEVICE_ID),
            CommonHeaders.CLIENT_VERSION to request.getHeader(CommonHeaders.CLIENT_VERSION),
            CommonHeaders.SESSION_ID to request.getHeader(CommonHeaders.SESSION_ID)
        ).filter { it.second.isNullOrEmpty() }
            .map { it.first }

        if (missingHeaders.isNotEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST)
            logger.error("${ErrorMessages.EMPTY_HEADER}: $missingHeaders")
            return
        }

        chain.doFilter(request, response)
    }
}
