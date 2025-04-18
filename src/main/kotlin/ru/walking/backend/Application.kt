package ru.walking.backend

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import ru.walking.backend.filters.TraceIdFilter

@SpringBootApplication
class Application {
    @Bean
    fun traceIdFilter(): FilterRegistrationBean<TraceIdFilter> {
        return FilterRegistrationBean<TraceIdFilter>().apply {
            filter = TraceIdFilter()
            order = Ordered.HIGHEST_PRECEDENCE
            addUrlPatterns("/*")
        }
    }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
