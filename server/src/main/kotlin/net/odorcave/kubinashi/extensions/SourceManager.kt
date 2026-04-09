package net.odorcave.kubinashi.extensions

import eu.kanade.tachiyomi.source.CatalogueSource
import io.github.oshai.kotlinlogging.KotlinLogging
import suwayomi.tachidesk.manga.impl.util.source.StubSource
import java.util.concurrent.ConcurrentHashMap

object SourceManager {
    private val logger = KotlinLogging.logger { }

    private val sourceCache = ConcurrentHashMap<Long, CatalogueSource>()

    private fun getCatalogueSource(sourceId: Long): CatalogueSource? {
        val cachedResult: CatalogueSource? = sourceCache[sourceId]
        return cachedResult
    }

    fun getCatalogueSourceOrNull(sourceId: Long): CatalogueSource? =
        try {
            getCatalogueSource(sourceId)
        } catch (e: Exception) {
            logger.warn(e) { "getCatalogueSource($sourceId) failed" }
            null
        }

    fun getCatalogueSourceOrStub(sourceId: Long): CatalogueSource = getCatalogueSourceOrNull(sourceId) ?: StubSource(sourceId)

    fun registerCatalogueSource(sourcePair: Pair<Long, CatalogueSource>) {
        sourceCache += sourcePair
    }

    fun unregisterCatalogueSource(sourceId: Long) {
        sourceCache.remove(sourceId)
    }

    fun unregisterAllCatalogueSources() {
        (sourceCache - 0L).forEach { (id, _) ->
            sourceCache.remove(id)
        }
    }
}
