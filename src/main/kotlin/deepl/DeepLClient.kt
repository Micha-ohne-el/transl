package deepl

import deepl.models.Translation
import deepl.models.TranslationResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class DeepLClient(
    private val authKey: String
) {
    suspend fun translate(text: String, targetLang: TargetLang, sourceLang: SourceLang? = null): String {
        val response = client.submitForm(
            url = "v2/translate",
            formParameters = Parameters.build {
                append("text", text)
                append("target_lang", targetLang.code)

                if (sourceLang != null) {
                    append("source_lang", sourceLang.code)
                }
            }
        )

        val translations: TranslationResponse = response.body()

        return translations.map(Translation::text).joinToString()
    }

    private val isFree = authKey.endsWith(":fx")

    private val baseUrl = if (isFree) {
        "https://api-free.deepl.com"
    } else {
        "https://api.deepl.com"
    }

    private val client = HttpClient {
        install(Logging)
        install(UserAgent) {
            agent = "TransL Discord Bot/0.0.0"
        }
        install(DefaultRequest) {
            url(baseUrl)
            header(HttpHeaders.Authorization, "DeepL-Auth-Key $authKey")
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 1)
        }
        install(ContentNegotiation) {
            json()
        }
    }
}
