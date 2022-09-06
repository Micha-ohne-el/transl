package discord.params

import dev.kord.rest.builder.interaction.NumberOptionBuilder
import discord.params.base.RequiredParam

class NumberParam(
    private val description: String
) : RequiredParam<Double>() {
    override val option
    get() = NumberOptionBuilder(name, description).apply {
        required = true
    }
}
