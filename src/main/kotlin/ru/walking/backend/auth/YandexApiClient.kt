package ru.walking.backend.auth

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.walking.backend.service.BaseLogger
import ru.walking.model.YandexPassportUserInfoEntity
import ru.walking.model.YandexPassportUserInfoResponse

@Component
class YandexApiClient : BaseLogger() {

    fun validateToken(token: String): YandexPassportUserInfoEntity? {
        try {
            val response = RestTemplate().getForEntity(
                "https://login.yandex.ru/info?oauth_token=$token",
                YandexPassportUserInfoResponse::class.java
            )
                .takeIf { it.body != null && it.statusCode == HttpStatus.OK }
                ?: throw AuthenticationServiceException("Тело пустое или ответ не OK(200)")

            return with(response.body!!) {
                YandexPassportUserInfoEntity(
                    id = id,
                    login = login,
                    email = defaultEmail,
                    firstName = firstName,
                    lastName = lastName,
                    avatar = null,
                )
            }
        } catch (e: AuthenticationServiceException) {
            logError(e.message.orEmpty(), e)
            return null
        } catch (e: RestClientException) {
            logError("Ошибка при получении токена, YandexApiClient", e)
            return null
        }
    }
}
