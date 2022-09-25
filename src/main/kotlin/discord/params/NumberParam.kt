package discord.params

import dev.kord.rest.builder.interaction.NumberOptionBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import discord.sanitize
import kotlinx.coroutines.runBlocking
import util.toSnakeCase

class NumberParam(
    private val description: String
) : RequiredParam<Double>() {
    override val option
    get() = NumberOptionBuilder(name, description).apply {
        required = true

        runBlocking {
            localizeName(this@NumberParam.name) {it.toSnakeCase().sanitize()}
            localizeDescription(this@NumberParam.description)
        }
    }
}
