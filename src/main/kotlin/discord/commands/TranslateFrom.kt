package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import discord.params.base.Suggestion
import discord.toTargetLang
import util.enumValueOfOrNull
import util.toLongOrNull

object TranslateFrom : ChatInputCommand() {
    override val name = "Translate from X"
    override val description = "Translate from any language to the server language!"

    private val sourceLanguage by StringParam("The language your original message is in.") { query ->
        SourceLang.find(query).map { lang ->
            Suggestion(lang.name, lang.name)
        }
    }

    private val sourceLang get() = enumValueOfOrNull<SourceLang>(sourceLanguage)

    private val message by StringParam("The message you want to translate.")

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()

        val guildId = interaction.data.guildId.toLongOrNull()

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        behavior.respond {
            content = TranslationRepo.translate(message, guildLang, sourceLang)
        }
    }
}
