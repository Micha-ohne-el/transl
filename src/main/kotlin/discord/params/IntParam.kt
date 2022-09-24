package discord.params

import dev.kord.rest.builder.interaction.IntegerOptionBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import discord.sanitize
import kotlinx.coroutines.runBlocking
import util.toSnakeCase

class IntParam(
    private val description: String
) : RequiredParam<Long>() {
    override val option
    get() = IntegerOptionBuilder(name, description).apply {
        required = true

        runBlocking {
            localizeName(name) {it.toSnakeCase().sanitize()}
            localizeDescription(description)
        }
    }
}
