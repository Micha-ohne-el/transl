package discord.commands

import GuildLangRepo
import TranslationRepo
import dev.kord.core.behavior.interaction.response.respond
import discord.MessageCommand
import discord.toTargetLang
import util.toLongOrNull

object TranslateMessage : MessageCommand() {
    override val name = "Translate message"

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val guildId = Translate.interaction.data.guildId.toLongOrNull()

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        behavior.respond {
            content = TranslationRepo.translate(interaction.messages.values.first().content, guildLang)
        }
    }
}
