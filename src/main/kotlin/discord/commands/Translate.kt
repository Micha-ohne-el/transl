package discord.commands

import GuildLangRepo
import TranslationRepo
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import discord.toTargetLang

object Translate : ChatInputCommand() {
    override val name = "Translate"
    override val description = "Translate a message to the chat!"

    private val message by StringParam("The message you want to translate.")

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()

        val guildIdSnowflake = interaction.data.guildId.value
        val guildId = guildIdSnowflake?.value?.toLong()

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        behavior.respond {
            content = TranslationRepo.translate(message, guildLang)
        }
    }
}
