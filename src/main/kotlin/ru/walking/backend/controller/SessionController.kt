package ru.walking.backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.walking.api.SessionApi
import ru.walking.backend.auth.YandexApiClient
import ru.walking.backend.common.ok
import ru.walking.backend.service.SessionService
import ru.walking.model.EndSessionV1Request
import ru.walking.model.EndSessionV1Response
import ru.walking.model.StartSessionV1Request
import ru.walking.model.StartSessionV1Response
import java.util.UUID

@RestController
class SessionController(
    private val sessionService: SessionService,
    private val authApiClient: YandexApiClient
) : SessionApi {
    override fun v1StartSessionPost(
        xIdempotencyKey: UUID,
        xDeviceId: String,
        xClientVersion: String,
        startSessionV1Request: StartSessionV1Request
    ): ResponseEntity<StartSessionV1Response> {
        return sessionService.startSession(startSessionV1Request = startSessionV1Request).ok()
    }

    override fun v1EndSessionPost(
        xSessionId: UUID,
        xIdempotencyKey: UUID,
        xDeviceId: String,
        xClientVersion: String,
        xRequestLanguage: String,
        endSessionV1Request: EndSessionV1Request
    ): ResponseEntity<EndSessionV1Response> {
        return super.v1EndSessionPost(
            xSessionId, xIdempotencyKey, xDeviceId, xClientVersion, xRequestLanguage, endSessionV1Request
        )
    }
}
