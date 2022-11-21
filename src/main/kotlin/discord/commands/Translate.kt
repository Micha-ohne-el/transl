package discord.commands

import GuildLangRepo
import TranslationRepo
import deepl.SourceLang
import deepl.TargetLang
import dev.kord.core.behavior.interaction.response.respond
import discord.ChatInputCommand
import discord.params.StringParam
import discord.params.base.Suggestion
import discord.toTargetLang
import util.respondWithError
import util.toLongOrNull

object Translate : ChatInputCommand() {
    override val name = "Translate"
    override val description = "Translate a message to the chat!"

    private val message by StringParam("The message you want to translate.")

    private val sourceLanguage by StringParam.Optional("Your language (automatically detected if omitted).") { query ->
        SourceLang.find(query).map { lang ->
            Suggestion(lang.name, lang.name)
        }
    }

    private val targetLanguage by StringParam.Optional("The language you want to translate to (the Server's language if omitted).") { query ->
        TargetLang.find(query).map { lang ->
            Suggestion(lang.name, lang.name)
        }
    }

    override suspend fun execute() {
        val userLang = interaction.locale!!.toTargetLang()

        val sourceLang = try {
            sourceLanguage?.let { enumValueOf<SourceLang>(it) }
        } catch (error: Throwable) {
            interaction.deferEphemeralResponse().respondWithError {
                description = TranslationRepo.translate(
                    "Language “${sourceLanguage}” is not (yet) supported, unfortunately.",
                    userLang,
                    SourceLang.English
                )
            }
            return
        }

        val guildId = interaction.data.guildId.toLongOrNull()

        val guildLang = (if (guildId != null) GuildLangRepo.getGuildLang(guildId) else null)
            ?: interaction.guildLocale?.toTargetLang()
            ?: GuildLangRepo.defaultGuildLang

        val targetLang = try {
            targetLanguage?.let { enumValueOf(it) } ?: guildLang
        } catch (error: Throwable) {
            interaction.deferEphemeralResponse().respondWithError {
                description = TranslationRepo.translate(
                    "Language “${targetLanguage}” is not (yet) supported, unfortunately.",
                    userLang,
                    SourceLang.English
                )
            }
            return
        }

        val behavior = interaction.deferPublicResponse()

        behavior.respond {
            content = TranslationRepo.translate(message, targetLang, sourceLang)
        }
    }
}
