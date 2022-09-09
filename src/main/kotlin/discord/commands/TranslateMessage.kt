package discord.commands

import GuildLangRepo
import TranslationRepo
import dev.kord.core.behavior.interaction.response.respond
import discord.MessageCommand
import discord.toTargetLang

object TranslateMessage : MessageCommand() {
    override val name = "translate"
    override val description = "Translate a previously sent message!"

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val guildIdSnowflake = interaction.data.guildId.value
        val guildId = guildIdSnowflake?.value?.toLong()

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        behavior.respond {
            content = TranslationRepo.translate(interaction.messages.values.first().content, guildLang)
        }
    }
}
