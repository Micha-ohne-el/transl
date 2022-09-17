package discord.commands

import GuildLangRepo
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand

object GetGuildLang : ChatInputCommand() {
    override val name = "getLang"
    override val description = "Get this Server's preferred language."

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val guildIdSnowflake = interaction.data.guildId.value
        val guildId = guildIdSnowflake?.value?.toLong()

        val currentLang = GuildLangRepo.getGuildLang(guildId!!) ?: GuildLangRepo.defaultGuildLang

        behavior.respond {
            content = "The current server language is set to $currentLang."
        }
    }
}
