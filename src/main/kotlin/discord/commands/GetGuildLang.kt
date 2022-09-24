package discord.commands

import GuildLangRepo
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import util.toLong

object GetGuildLang : ChatInputCommand() {
    override val name = "Get language"
    override val description = "Get this Server's preferred language."

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val guildId = interaction.data.guildId.toLong()

        val currentLang = GuildLangRepo.getGuildLangOrDefault(guildId)

        behavior.respond {
            content = "The current server language is set to $currentLang."
        }
    }
}
