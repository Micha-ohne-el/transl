package discord.params

import dev.kord.rest.builder.interaction.StringChoiceBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import kotlinx.coroutines.runBlocking

class StringParam(
    private val description: String,
    private val allowedChoices: List<String>? = null
) : RequiredParam<String>() {
    override val option
    get() = StringChoiceBuilder(name, description).apply {
        required = true

        if (allowedChoices != null) {
            for (choiceName in allowedChoices.subList(0, 24)) { // Discord limits the amount of choices to 25.
                choice(choiceName, choiceName) {
                    runBlocking {
                        localizeName(choiceName)
                    }
                }
            }
        }

        runBlocking {
            localizeName(name, snakeCase = true)
            localizeDescription(description)
        }
    }
}
