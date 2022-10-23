package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang
import deepl.TargetLang
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import discord.params.base.Suggestion
import util.enumValueOfOrNull
import util.toLong

object SetGuildLang : ChatInputCommand() {
    override val name = "Set language"
    override val description = "Set this Server's preferred language."

    override val permissions = setOf(Permission.ManageGuild)

    private val language by StringParam("The language people should talk in.") { query ->
        TargetLang.find(query).map { lang ->
            Suggestion(lang.name, lang.name)
        }
    }

    private val lang get() = enumValueOfOrNull<TargetLang>(language)

    override suspend fun execute() {
        val behavior = interaction.deferEphemeralResponse()

        val guildId = interaction.data.guildId.toLong()

        if (lang == null) {
            behavior.respond {
                content = TranslationRepo.translate(
                    "Language “$language” is not (yet) supported, unfortunately.",
                    GuildLangRepo.getGuildLangOrDefault(guildId),
                    SourceLang.English
                )
            }
            return
        }

        val oldGuildLang = GuildLangRepo.getGuildLangOrDefault(guildId)

        GuildLangRepo.setGuildLang(guildId, lang!!)

        behavior.respond {
            content = TranslationRepo.translate(
                "The server language has been set to “$lang”.",
                oldGuildLang,
                SourceLang.English
            ) + "\n" + TranslationRepo.translate(
                "The server language has been set to “$lang”.",
                GuildLangRepo.getGuildLangOrDefault(guildId),
                SourceLang.English
            )
        }
    }
}
