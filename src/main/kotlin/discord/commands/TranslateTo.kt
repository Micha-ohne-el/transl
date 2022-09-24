package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import discord.toTargetLang
import util.enumValueOfOrNull
import util.toLongOrNull

object TranslateTo : ChatInputCommand() {
    override val name = "Translate to X"
    override val description = "Translate from the server language to any other language!"

    private val targetLanguage by StringParam(
        "The language your message should be translated into.",
        TargetLang.values().map(TargetLang::name)
    )

    private val targetLang get() = enumValueOfOrNull<TargetLang>(targetLanguage)

    private val message by StringParam("The message you want to translate.")

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()

        val guildId = interaction.data.guildId.toLongOrNull()

        if (targetLang == null) {
            behavior.respond {
                content = TranslationRepo.translate(
                    "Language “$targetLanguage” is not (yet) supported, unfortunately.",
                    GuildLangRepo.getGuildLangOrDefault(guildId ?: 0),
                    SourceLang.English
                )
            }
            return
        }

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        behavior.respond {
            content = TranslationRepo.translate(message, targetLang!!, guildLang.toSourceLang())
        }
    }
}
