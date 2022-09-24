package discord

import TranslationRepo
import deepl.SourceLang
import dev.kord.common.Locale
import dev.kord.rest.builder.interaction.LocalizedDescriptionBuilder
import dev.kord.rest.builder.interaction.LocalizedNameBuilder

suspend fun LocalizedNameBuilder.localizeName(
    name: String,
    sourceLang: SourceLang = SourceLang.English,
    transform: (String) -> String = {it}
) {
    nameLocalizations = Locale.ALL.associateWithTo(mutableMapOf()) {
        val translation = TranslationRepo.translate(name, it.toTargetLang(), sourceLang)

        transform(translation)
    }
}

suspend fun LocalizedDescriptionBuilder.localizeDescription(
    description: String,
    sourceLang: SourceLang = SourceLang.English,
    transform: (String) -> String = {it}
) {
    descriptionLocalizations = Locale.ALL.associateWithTo(mutableMapOf()) {
        val translation = TranslationRepo.translate(description, it.toTargetLang(), sourceLang)

        transform(translation)
    }
}
