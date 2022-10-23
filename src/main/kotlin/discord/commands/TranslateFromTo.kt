package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import discord.params.base.Suggestion
import util.enumValueOfOrNull
import util.toLong

object TranslateFromTo : ChatInputCommand() {
    override val name = "Translate between"
    override val description = "Translate from any language to any other language!"

    private val sourceLanguage by StringParam("The language your original message is in.") { query ->
        SourceLang.find(query).map { lang ->
            Suggestion(lang.name, lang.name)
        }
    }

    private val sourceLang get() = enumValueOfOrNull<SourceLang>(sourceLanguage)

    private val targetLanguage by StringParam("The language your message should be translated into.") { query ->
        TargetLang.find(query).map { lang ->
            Suggestion(lang.name, lang.name)
        }
    }

    private val targetLang get() = enumValueOfOrNull<TargetLang>(targetLanguage)

    private val message by StringParam("The message you want to translate.")

    override suspend fun execute() {
        if (targetLang == null) {
            interaction.deferEphemeralResponse().respond {
                content = TranslationRepo.translate(
                    "Language “$targetLanguage” is not (yet) supported, unfortunately.",
                    GuildLangRepo.getGuildLangOrDefault(interaction.data.guildId.toLong()),
                    SourceLang.English
                )
            }
            return
        }

        val behavior = interaction.deferPublicResponse()

        behavior.respond {
            content = TranslationRepo.translate(message, targetLang!!, sourceLang)
        }
    }
}
