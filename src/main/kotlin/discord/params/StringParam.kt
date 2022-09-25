package discord.params

import dev.kord.rest.builder.interaction.StringChoiceBuilder
import discord.localizeDescription
import discord.localizeName
import discord.params.base.RequiredParam
import discord.sanitize
import kotlinx.coroutines.runBlocking
import util.toSnakeCase
import util.toSpaceCase
import util.toTitleSpaceCase

class StringParam(
    private val description: String,
    private val allowedChoices: List<String>? = null
) : RequiredParam<String>() {
    override val option
    get() = StringChoiceBuilder(name, description).apply {
        required = true

        if (allowedChoices != null) {
            for (choiceName in allowedChoices.subList(0, 24)) { // Discord limits the amount of choices to 25.
                choice(choiceName.toTitleSpaceCase(), choiceName) {
                    runBlocking {
                        localizeName(choiceName.toTitleSpaceCase())
                    }
                }
            }
        }

        runBlocking {
            localizeName(this@StringParam.name.toSpaceCase()) {it.toSnakeCase().sanitize()}
            localizeDescription(this@StringParam.description)
        }
    }
}
