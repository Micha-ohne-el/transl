package deepl

import deepl.models.Translation
import deepl.models.TranslationResponse
import dev.kord.rest.request.KtorRequestException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("DeepL")

class DeeplClient(
    private val authKey: String
) {
    suspend fun translate(text: String, targetLang: TargetLang, sourceLang: SourceLang? = null): String {
        log.info("Translating from $sourceLang to $targetLang: ${text.quote()}")

        try {
             val response = client.submitForm(
                url = "v2/translate",
                formParameters = Parameters.build {
                    append("text", text)
                    append("target_lang", targetLang.code)

                    if (targetLang.hasFormality) {
                        append("formality", "less")
                    }

                    if (sourceLang != null) {
                        append("source_lang", sourceLang.code)
                    }
                }
            )

            val translations: TranslationResponse = response.body()

            return translations.map(Translation::text).joinToString()
        } catch (error: Throwable) {
            if (error is KtorRequestException && error.status.code == 456) { // source: https://www.deepl.com/docs-api/api-access/error-handling/
                log.error("DeepL quota exceeded for this month!!!")
            } else {
                log.error("An unexpected error occurred during translation.")
                log.error(error.stackTraceToString())
            }

            throw error
        }
    }

    private val isFree = authKey.endsWith(":fx")

    private val baseUrl = if (isFree) {
        "https://api-free.deepl.com"
    } else {
        "https://api.deepl.com"
    }

    private val client = HttpClient {
        install(UserAgent) {
            agent = "TransL Discord Bot"
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
        install(HttpRequestRetry) {
            maxRetries = 4
            exponentialDelay()
            retryIf { _, response ->
                response.status == HttpStatusCode.TooManyRequests
            }
        }
    }
}
