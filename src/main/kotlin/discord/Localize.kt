package discord

import TranslationRepo
import deepl.SourceLang
import dev.kord.common.Locale
import dev.kord.rest.builder.interaction.LocalizedDescriptionBuilder
import dev.kord.rest.builder.interaction.LocalizedNameBuilder
import util.toSnakeCase

suspend fun LocalizedNameBuilder.localizeName(
    name: String,
    sourceLang: SourceLang = SourceLang.English,
    snakeCase: Boolean = false
) {
    nameLocalizations = Locale.ALL.associateWithTo(mutableMapOf()) {
        val translation = TranslationRepo.translate(name, it.toTargetLang(), sourceLang)

        return@associateWithTo if (snakeCase) {
            translation.toSnakeCase()
        } else {
            translation
        }
    }
}

suspend fun LocalizedDescriptionBuilder.localizeDescription(
    description: String,
    sourceLang: SourceLang = SourceLang.English,
    snakeCase: Boolean = false
) {
    descriptionLocalizations = Locale.ALL.associateWithTo(mutableMapOf()) {
        val translation = TranslationRepo.translate(description, it.toTargetLang(), sourceLang)

        return@associateWithTo if (snakeCase) {
            translation.toSnakeCase()
        } else {
            translation
        }
    }
}
