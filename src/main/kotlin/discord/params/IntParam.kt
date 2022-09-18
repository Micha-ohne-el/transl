package discord.params

import dev.kord.rest.builder.interaction.IntegerOptionBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import kotlinx.coroutines.runBlocking

class IntParam(
    private val description: String
) : RequiredParam<Long>() {
    override val option
    get() = IntegerOptionBuilder(name, description).apply {
        required = true

        runBlocking {
            localizeName(name, snakeCase = true)
            localizeDescription(description)
        }
    }
}
