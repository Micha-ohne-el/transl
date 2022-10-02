package discord.params

import dev.kord.core.behavior.interaction.suggestNumber
import dev.kord.core.entity.interaction.AutoCompleteInteraction
import dev.kord.rest.builder.interaction.NumberOptionBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import discord.params.base.Suggestion
import discord.sanitize
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

        interaction.suggestNumber {
            for (suggestion in suggester.invoke(interaction.focusedOption.value).take(25)) {
                choice(suggestion.name.toTitleSpaceCase(), suggestion.value) {
                    localizeName(suggestion.name.toTitleSpaceCase())
                }
            }
        }
    }
}
