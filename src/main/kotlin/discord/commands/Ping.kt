package discord.commands

import dev.kord.core.behavior.interaction.response.respond
import discord.Command

object Ping : Command() {
    override val name = "ping"
    override val description = "Pong?"

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()
        behavior.respond {
            content = "Pong!"
        }
    }
}
