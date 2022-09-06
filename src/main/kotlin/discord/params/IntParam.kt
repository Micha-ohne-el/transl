package discord.params

import dev.kord.rest.builder.interaction.IntegerOptionBuilder
import discord.params.base.RequiredParam

class IntParam(
    private val description: String
) : RequiredParam<Long>() {
    override val option
    get() = IntegerOptionBuilder(name, description).apply {
        required = true
    }
}
