package discord.params

import dev.kord.core.behavior.interaction.suggestString
import dev.kord.core.entity.interaction.AutoCompleteInteraction
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import discord.params.base.Suggestion
import discord.sanitize
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

        interaction.suggestString {
            for (suggestion in suggester.invoke(interaction.focusedOption.value).take(25)) {
                choice(suggestion.name.toTitleSpaceCase(), suggestion.value) {
                    localizeName(suggestion.name.toTitleSpaceCase())
                }
            }
        }
    }
}
