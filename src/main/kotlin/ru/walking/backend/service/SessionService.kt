package ru.walking.backend.service

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import ru.walking.backend.auth.YandexApiClient
import ru.walking.backend.common.ifNull
import ru.walking.model.ResponseFailDataV1
import ru.walking.model.ResponseStatusV1
import ru.walking.model.SessionEntity
import ru.walking.model.StartSessionSuccessData
import ru.walking.model.StartSessionV1Request
import ru.walking.model.StartSessionV1Response
import ru.walking.model.YandexPassportUserInfoEntity
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class SessionService(
    private val cacheManager: CacheManager,
    private val authApiClient: YandexApiClient,
) : BaseService() {

    fun startSession(startSessionV1Request: StartSessionV1Request): StartSessionV1Response {
        val userInfo = authApiClient.validateToken(startSessionV1Request.token)
            ?: return StartSessionV1Response(
                status = ResponseStatusV1.FAIL,
                failData = ResponseFailDataV1(title = "")
            )

        val session = createSession(userInfo = userInfo)
        return StartSessionV1Response(
            status = ResponseStatusV1.SUCCESS,
            successData = StartSessionSuccessData(
                id = session.id,
                startedAt = session.startedAt,
                expiresAt = session.expiresAt,
            ),
        )
    }

    private fun createSession(userInfo: YandexPassportUserInfoEntity): SessionEntity {
        val sessionKey = UUID.randomUUID()

        val startPoint = Instant.now()
        val session = SessionEntity(
            id = sessionKey,
            startedAt = startPoint.epochSecond,
            expiresAt = startPoint.plus(SESSION_TIMEOUT_HOURS, ChronoUnit.HOURS).epochSecond,
            userInfo = userInfo,
        )
        cacheManager.getCache(CACHE_SESSION_TAG)?.put(sessionKey, session)

        return session
    }

    fun validateSession(sessionKey: String, deviceId: String, clientVersion: String): Boolean {
        val data = cacheManager.getCache(CACHE_SESSION_TAG)?.get(UUID.fromString(sessionKey), SessionEntity::class.java)
            .ifNull {
                logError("Ожидаемый ключ сессии [$sessionKey] не был найден")
            }
            ?: return false
        return data.expiresAt > Instant.now().epochSecond
    }

    private companion object {
        const val CACHE_SESSION_TAG = "sessions"
        const val SESSION_TIMEOUT_HOURS = 1L
    }
}
