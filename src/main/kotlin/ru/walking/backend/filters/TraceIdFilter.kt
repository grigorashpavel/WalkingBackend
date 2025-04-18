package ru.walking.backend.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.jboss.logging.MDC
import org.springframework.web.filter.OncePerRequestFilter
import ru.walking.backend.common.CommonHeaders
import ru.walking.backend.common.Constants
import java.util.UUID

class TraceIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val traceId = UUID.randomUUID().toString()

        MDC.put(Constants.TRACE_ID_KEY, traceId)
        response.setHeader(CommonHeaders.TRACE_ID, traceId)

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}
