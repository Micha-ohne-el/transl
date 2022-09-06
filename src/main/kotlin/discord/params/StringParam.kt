package discord.params

import dev.kord.rest.builder.interaction.OptionsBuilder
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import discord.params.base.OptionalParam
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

    inner class Optional : OptionalParam<String>() {
        override val option: OptionsBuilder
        get() = StringChoiceBuilder(name, description).apply {
            required = false

            if (allowedChoices != null) {
                for (c in allowedChoices) {
                    choice(c, c)
                }
            }
        }
    }
}
