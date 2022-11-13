package discord.params

import TranslationRepo
import deepl.SourceLang
import dev.kord.common.Locale
import dev.kord.core.behavior.interaction.suggestString
import dev.kord.core.entity.interaction.AutoCompleteInteraction
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.OptionalParam
import discord.params.base.RequiredParam
import discord.params.base.Suggestion
import discord.sanitize
import discord.toTargetLang
import kotlinx.coroutines.runBlocking
import util.toSnakeCase
import util.toSpaceCase
import util.toTitleSpaceCase

class StringParam(
    private val description: String,
    override val suggester: ((query: String) -> Iterable<Suggestion<String>>)? = null
) : RequiredParam<String>() {
    override val option
    get() = StringChoiceBuilder(name, description).apply {
        required = true

        autocomplete = suggester != null

        runBlocking {
            localizeName(this@StringParam.name.toSpaceCase()) {it.toSnakeCase().sanitize()}
            localizeDescription(this@StringParam.description)
        }
    }

    override suspend fun suggest(interaction: AutoCompleteInteraction) {
        if (suggester == null) {
            return
        }

        val userLocale = interaction.locale ?: Locale.ENGLISH_UNITED_STATES

        interaction.suggestString {
            for (suggestion in suggester.invoke(interaction.focusedOption.value).take(25)) {
                val suggestionName = suggestion.name.toTitleSpaceCase()

                choice(suggestionName, suggestion.value) {
                    if (nameLocalizations == null) {
                        nameLocalizations = mutableMapOf()
                    }
                    nameLocalizations!![userLocale] =
                        TranslationRepo.translate(suggestionName, userLocale.toTargetLang(), SourceLang.English)
                }
            }
        }
    }

    class Optional(
        private val description: String,
        override val suggester: ((query: String) -> Iterable<Suggestion<String?>>)? = null
    ) : OptionalParam<String>() {
        override val option
            get() = StringChoiceBuilder(name, description).apply {
                required = false

                autocomplete = suggester != null

                runBlocking {
                    localizeName(this@Optional.name.toSpaceCase()) { it.toSnakeCase().sanitize() }
                    localizeDescription(this@Optional.description)
                }
            }

        override suspend fun suggest(interaction: AutoCompleteInteraction) {
            if (suggester == null) {
                return
            }

            val userLocale = interaction.locale ?: Locale.ENGLISH_UNITED_STATES

            interaction.suggestString {
                for (suggestion in suggester.invoke(interaction.focusedOption.value)
                    .filter { suggestion -> suggestion.value != null }
                    .take(25)) {
                    val suggestionName = suggestion.name.toTitleSpaceCase()

                    choice(suggestionName, suggestion.value!!) {
                        if (nameLocalizations == null) {
                            nameLocalizations = mutableMapOf()
                        }
                        nameLocalizations!![userLocale] =
                            TranslationRepo.translate(suggestionName, userLocale.toTargetLang(), SourceLang.English)
                    }
                }
            }
        }
    }
}
