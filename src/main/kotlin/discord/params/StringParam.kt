package discord.params

import dev.kord.rest.builder.interaction.StringChoiceBuilder
import discord.params.base.RequiredParam

class StringParam(
    private val description: String,
    private val allowedChoices: List<String>? = null
) : RequiredParam<String>() {
    override val option
    get() = StringChoiceBuilder(name, description).apply {
        required = true

        if (allowedChoices != null) {
            for (c in allowedChoices) {
                choice(c, c)
            }
        }
    }
}
