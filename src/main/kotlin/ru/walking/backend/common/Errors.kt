package ru.walking.backend.common

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatusCode

fun HttpServletResponse.sendError(status: HttpStatusCode) = sendError(status.value())

object ErrorMessages {
    const val EMPTY_HEADER = "Обязательные заголовки пусты"
}
