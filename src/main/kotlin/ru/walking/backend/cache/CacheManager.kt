package ru.walking.backend.cache

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import ru.walking.model.SessionEntity
import java.util.UUID
import java.util.concurrent.Callable

object Cache : Cache {
    private val sessionCache = hashMapOf<UUID, SessionEntity>()

    override fun getName(): String = "session-cache"

    override fun getNativeCache(): Any = Unit

    override fun get(key: Any): Cache.ValueWrapper? = null

    override fun <T : Any?> get(key: Any, type: Class<T>?): T? = sessionCache[key as UUID] as? T

    override fun <T : Any?> get(key: Any, valueLoader: Callable<T>): T? = sessionCache[key as UUID] as? T

    override fun put(key: Any, value: Any?) {
        sessionCache[key as UUID] = value as SessionEntity
    }

    override fun evict(key: Any) = Unit

    override fun clear() = sessionCache.clear()
}

@Component
class CacheManager : CacheManager {

    override fun getCache(name: String): Cache? = ru.walking.backend.cache.Cache.takeIf { it.name == name }

    override fun getCacheNames(): MutableCollection<String> = mutableListOf(ru.walking.backend.cache.Cache.name)
}
