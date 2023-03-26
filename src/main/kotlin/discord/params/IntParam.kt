package discord.params

import TranslationRepo
import deepl.SourceLang
import dev.kord.common.Locale
import dev.kord.core.behavior.interaction.suggestInteger
import dev.kord.core.entity.interaction.AutoCompleteInteraction
import dev.kord.rest.builder.interaction.IntegerOptionBuilder
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

class IntParam(
    private val description: String,
    override val suggester: ((query: String) -> Iterable<Suggestion<Long>>)? = null
) : RequiredParam<Long>() {
    override val option
    get() = IntegerOptionBuilder(name, description).apply {
        required = true

        autocomplete = suggester != null

        runBlocking {
            localizeName(this@IntParam.name.toSpaceCase()) {it.toSnakeCase().sanitize()}
            localizeDescription(this@IntParam.description)
        }
    }

    override suspend fun suggest(interaction: AutoCompleteInteraction) {
        if (suggester == null) {
            return
        }

        val userLocale = interaction.locale ?: Locale.ENGLISH_UNITED_STATES

        interaction.suggestInteger {
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
