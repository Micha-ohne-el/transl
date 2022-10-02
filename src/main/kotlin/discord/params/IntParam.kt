package discord.params

import dev.kord.core.behavior.interaction.suggestInt
import dev.kord.core.entity.interaction.AutoCompleteInteraction
import dev.kord.rest.builder.interaction.IntegerOptionBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import discord.params.base.Suggestion
import discord.sanitize
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

        interaction.suggestInt {
            for (suggestion in suggester.invoke(interaction.focusedOption.value).take(25)) {
                choice(suggestion.name.toTitleSpaceCase(), suggestion.value) {
                    localizeName(suggestion.name.toTitleSpaceCase())
                }
            }
        }
    }
}
