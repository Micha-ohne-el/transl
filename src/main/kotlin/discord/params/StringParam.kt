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
            for (c in allowedChoices.subList(0, 24)) { // Discord limits the amount of choices to 25.
                choice(c, c)
            }
        }
    }
}
