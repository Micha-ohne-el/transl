package discord.params

import TranslationRepo
import deepl.SourceLang
import dev.kord.common.Locale
import dev.kord.core.behavior.interaction.suggestNumber
import dev.kord.core.entity.interaction.AutoCompleteInteraction
import dev.kord.rest.builder.interaction.NumberOptionBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import discord.params.base.Suggestion
import discord.sanitize
import discord.toTargetLang
import kotlinx.coroutines.runBlocking
import util.toSnakeCase
import util.toSpaceCase
import util.toTitleSpaceCase

class NumberParam(
    private val description: String,
    override val suggester: ((query: String) -> Iterable<Suggestion<Double>>)? = null
) : RequiredParam<Double>() {
    override val option
    get() = NumberOptionBuilder(name, description).apply {
        required = true

        autocomplete = suggester != null

        runBlocking {
            localizeName(this@NumberParam.name.toSpaceCase()) {it.toSnakeCase().sanitize()}
            localizeDescription(this@NumberParam.description)
        }
    }

    override suspend fun suggest(interaction: AutoCompleteInteraction) {
        if (suggester == null) {
            return
        }

        val userLocale = interaction.locale ?: Locale.ENGLISH_UNITED_STATES

        interaction.suggestNumber {
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
}
