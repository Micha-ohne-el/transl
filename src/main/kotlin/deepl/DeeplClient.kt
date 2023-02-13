package deepl

import deepl.models.Translation
import deepl.models.TranslationResponse
import dev.kord.rest.request.KtorRequestException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.quote
import io.ktor.serialization.kotlinx.json.json
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
                    append("formality", "prefer_less")

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
