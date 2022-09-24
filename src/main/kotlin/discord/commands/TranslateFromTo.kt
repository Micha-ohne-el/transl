package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import util.enumValueOfOrNull
import util.toLong

object TranslateFromTo : ChatInputCommand() {
    override val name = "Translate from X to Y"
    override val description = "Translate from any language to any other language!"

    private val sourceLanguage by StringParam(
        "The language your original message is in.",
        SourceLang.values().map(SourceLang::name)
    )

    private val sourceLang get() = enumValueOfOrNull<SourceLang>(sourceLanguage)

    private val targetLanguage by StringParam(
        "The language your message should be translated into.",
        TargetLang.values().map(TargetLang::name)
    )

    private val targetLang get() = enumValueOfOrNull<TargetLang>(targetLanguage)

    private val message by StringParam("The message you want to translate.")

    override suspend fun execute() {
        val behavior = interaction.deferPublicResponse()


        if (targetLang == null) {
            behavior.respond {
                content = TranslationRepo.translate(
                    "Language “$targetLanguage” is not (yet) supported, unfortunately.",
                    GuildLangRepo.getGuildLangOrDefault(interaction.data.guildId.toLong()),
                    SourceLang.English
                )
            }
            return
        }

        behavior.respond {
            content = TranslationRepo.translate(message, targetLang!!, sourceLang)
        }
    }
}
