package discord

import dev.kord.core.entity.interaction.ApplicationCommandInteraction

interface Command {
    val name: String
    val description: String

    suspend fun execute(interaction: ApplicationCommandInteraction)
}
