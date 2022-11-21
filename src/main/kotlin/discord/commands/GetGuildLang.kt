package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang
import dev.kord.common.entity.Permission
import discord.ChatInputCommand
import util.respondNeutrally
import util.toLong

object GetGuildLang : ChatInputCommand() {
    override val name = "Get language"
    override val description = "Get this Server's preferred language."

    override val permissions = setOf(Permission.ManageGuild)

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val guildId = interaction.data.guildId.toLong()

        val currentLang = GuildLangRepo.getGuildLangOrDefault(guildId)

        behavior.respondNeutrally {
            description = TranslationRepo.translate(
                "The current server language is set to “$currentLang”.",
                GuildLangRepo.getGuildLangOrDefault(guildId),
                SourceLang.English
            )
        }
    }
}
