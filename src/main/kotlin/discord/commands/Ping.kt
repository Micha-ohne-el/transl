package discord.commands

import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import discord.Command

object Ping : Command {
    override val name = "ping"
    override val description = "Pong?"

    override suspend fun execute(interaction: ApplicationCommandInteraction) {
        val behavior = interaction.deferPublicResponse()
        behavior.respond {
            content = "Pong!"
        }
    }
}
