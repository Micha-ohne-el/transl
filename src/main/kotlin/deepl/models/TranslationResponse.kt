package deepl.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationResponse(
    val translations: List<Translation>
) : List<Translation> by translations

@Serializable
data class Translation(
    @SerialName("detected_source_language")
    val detectedSourceLang: String,

    val text: String
)
