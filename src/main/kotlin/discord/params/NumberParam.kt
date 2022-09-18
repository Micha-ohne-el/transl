package discord.params

import dev.kord.rest.builder.interaction.NumberOptionBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import kotlinx.coroutines.runBlocking

class NumberParam(
    private val description: String
) : RequiredParam<Double>() {
    override val option
    get() = NumberOptionBuilder(name, description).apply {
        required = true

        runBlocking {
            localizeName(name, snakeCase = true)
            localizeDescription(description)
        }
    }
}
