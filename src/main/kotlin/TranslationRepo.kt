
import deepl.DeeplClient
import deepl.SourceLang
import deepl.TargetLang

object TranslationRepo {
    suspend fun translate(text: String, targetLang: TargetLang, sourceLang: SourceLang? = null): String {
        return cache.getOrPut(Request(text, targetLang, sourceLang)) {
            deepl.translate(text, targetLang, sourceLang)
        }
    }


    private data class Request(
        val text: String,
        val targetLang: TargetLang,
        val sourceLang: SourceLang?
    )

    private val cache = object : LinkedHashMap<Request, String>() {
        val limit = Vault.translationCacheSize

        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Request, String>?): Boolean {
            return size >= limit
        }
    }

    private val deepl = DeeplClient(Vault.deeplAuthKey)
}
